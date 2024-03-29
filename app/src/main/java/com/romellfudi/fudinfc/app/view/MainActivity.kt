/*
 * Copyright (c) 2020. BoostTag E.I.R.L. Romell D.Z.
 * All rights reserved
 * porfile.romellfudi.com
 */
package com.romellfudi.fudinfc.app.view

import android.app.ProgressDialog
import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import com.romellfudi.fudinfc.app.R
import com.romellfudi.fudinfc.gear.NfcAct
import com.romellfudi.fudinfc.gear.interfaces.OpCallback
import com.romellfudi.fudinfc.gear.interfaces.TaskCallback
import com.romellfudi.fudinfc.util.async.WriteCallbackNfc
import com.romellfudi.fudinfc.util.interfaces.NfcReadUtility
import com.romellfudi.fudinfc.util.interfaces.NfcWriteUtility
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.core.parameter.parametersOf
import java.math.BigInteger

class MainActivity : NfcAct(), KoinComponent {

    private val mProgressDialog: ProgressDialog by inject { parametersOf(this@MainActivity) }

    private val mNfcReadUtility: NfcReadUtility by inject()

    private val mTaskCallback: TaskCallback by inject()

    private val handler: Handler by inject()

    var mOpCallback: OpCallback? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        emailButton.setOnClickListener {
            if (emailText != null) {
                val text = edit_emailText.text.toString()
                mOpCallback = object : OpCallback {
                    override fun performWrite(writeUtility: NfcWriteUtility?): Boolean {
                        return writeUtility?.run {
                            writeEmailToTagFromIntent(
                                text,
                                null,
                                null,
                                intent
                            )
                        }
                            ?: false
                    }
                }
                showDialog()
            }
        }
        smsButton.setOnClickListener {
            if (smsText != null) {
                val text = edit_smsText.text.toString()
                mOpCallback = object : OpCallback {
                    override fun performWrite(writeUtility: NfcWriteUtility?): Boolean {
                        return writeUtility?.run { writeSmsToTagFromIntent(text, null, intent) }
                            ?: false
                    }
                }
                showDialog()
            }
        }
        geoButton.setOnClickListener {
            if (latitudeText != null && longitudeText != null) {
                val longitude = edit_latitudeText.text.toString().toDouble()
                val latitude = edit_longitudeText.text.toString().toDouble()
                mOpCallback = object : OpCallback {
                    override fun performWrite(writeUtility: NfcWriteUtility?): Boolean {
                        return writeUtility?.run {
                            writeGeolocationToTagFromIntent(
                                latitude,
                                longitude,
                                intent
                            )
                        } ?: false
                    }
                }
                showDialog()
            }
        }
        uriButton?.setOnClickListener {
            val uriText = edit_input_text_uri_target.text.toString()
            mOpCallback = object : OpCallback {
                override fun performWrite(writeUtility: NfcWriteUtility?): Boolean {
                    return writeUtility?.run { writeUriToTagFromIntent(uriText, intent) } ?: false
                }
            }
            showDialog()
        }
        telButton.setOnClickListener {
            val telText = edit_input_text_tel_target.text.toString()
            mOpCallback = object : OpCallback {
                override fun performWrite(writeUtility: NfcWriteUtility?): Boolean {
                    return writeUtility?.run { writeTelToTagFromIntent(telText, intent) } ?: false
                }
            }
            showDialog()
        }
        bluetoothButton.setOnClickListener {
            val text = edit_bluetoothInput.text.toString()
            mOpCallback = object : OpCallback {
                override fun performWrite(writeUtility: NfcWriteUtility?): Boolean {
                    return writeUtility?.run { writeBluetoothAddressToTagFromIntent(text, intent) }
                        ?: false
                }
            }
            showDialog()
        }
        enableBeam()
    }

    override fun onPause() {
        super.onPause()
        nfcAdapter?.disableForegroundDispatch(this)
    }

    public override fun onNewIntent(paramIntent: Intent) {
        super.onNewIntent(paramIntent)
        if (mProgressDialog.isShowing) {
            mOpCallback?.let { WriteCallbackNfc(mTaskCallback, it).executeWriteOperation() }
            mOpCallback = null
        } else {
            val dataFull =
                "my mac: " + getMAC(intent.getParcelableExtra(NfcAdapter.EXTRA_TAG) as? Tag)
            mNfcReadUtility.readFromTagWithMap(paramIntent)?.values
                ?.fold(dataFull) { full, st -> full + "\n${st}" }
                .also { Toast.makeText(this, it, Toast.LENGTH_SHORT).show() }
        }
    }

    override fun onDestroy() {
        dismissDialog()
        super.onDestroy()
    }

    private fun showDialog() {
        progressbar.visibility = View.VISIBLE
        handler.postDelayed(this::dismissDialog, 10000)
        mProgressDialog.setCancelable(false)
        mProgressDialog.setTitle(R.string.progressdialog_waiting_for_tag)
        mProgressDialog.setMessage(getString(R.string.progressdialog_waiting_for_tag_message))
        mProgressDialog.show()
    }

    private fun dismissDialog() {
        mProgressDialog.dismiss()
        progressbar.visibility = View.INVISIBLE
    }

    private fun getMAC(tag: Tag?): String =
        Regex("(.{2})").replace(
            String.format(
                "%0" + ((tag?.id?.size ?: 0) * 2).toString() + "X",
                BigInteger(1, tag?.id ?: byteArrayOf())
            ), "$1:"
        ).dropLast(1)
}