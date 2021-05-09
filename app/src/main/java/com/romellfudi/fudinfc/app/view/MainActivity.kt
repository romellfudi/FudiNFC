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
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.core.parameter.parametersOf
import java.math.BigInteger

class MainActivity : NfcAct(), KoinComponent {

    private val mProgressDialog: ProgressDialog by inject { parametersOf(this@MainActivity)}

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
                mOpCallback = OpCallback { it.writeEmailToTagFromIntent(text, null, null, intent) }
                showDialog()
            }
        }
        smsButton.setOnClickListener {
            if (smsText != null) {
                val text = edit_smsText.text.toString()
                mOpCallback = OpCallback { it.writeSmsToTagFromIntent(text, null, intent) }
                showDialog()
            }
        }
        geoButton.setOnClickListener {
            if (latitudeText != null && longitudeText != null) {
                val longitude = edit_latitudeText.text.toString().toDouble()
                val latitude = edit_longitudeText.text.toString().toDouble()
                mOpCallback = OpCallback { it.writeGeolocationToTagFromIntent(latitude, longitude, intent) }
                showDialog()
            }
        }
        uriButton?.setOnClickListener {
            val uriText = edit_input_text_uri_target.text.toString()
            mOpCallback = OpCallback { it.writeUriToTagFromIntent(uriText, intent) }
            showDialog()
        }
        telButton.setOnClickListener {
            val telText = edit_input_text_tel_target.text.toString()
            mOpCallback = OpCallback { it.writeTelToTagFromIntent(telText, intent) }
            showDialog()
        }
        bluetoothButton.setOnClickListener {
            val text = edit_bluetoothInput.text.toString()
            mOpCallback = OpCallback { it.writeBluetoothAddressToTagFromIntent(text, intent) }
            showDialog()
        }
        enableBeam()
    }

    override fun onPause() {
        super.onPause()
        if (nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(this)
        }
    }

    public override fun onNewIntent(paramIntent: Intent) {
        super.onNewIntent(paramIntent)
        if (mOpCallback != null && mProgressDialog.isShowing) {
            WriteCallbackNfc(mTaskCallback, mOpCallback!!).executeWriteOperation()
            mOpCallback = null
        } else {
            var dataFull = "my mac: " +
                    getMAC(intent.getParcelableExtra(NfcAdapter.EXTRA_TAG) as Tag)
            mNfcReadUtility.readFromTagWithMap(paramIntent).values
                    .fold(dataFull) { full, st -> full + "\n${st}" }
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

    private fun getMAC(tag: Tag): String {
        val byteArrayToHexString = String.format("%0" + (tag.id.size * 2).toString() + "X", BigInteger(1, tag.id))
        val regex = Regex("(.{2})")
        return regex.replace(byteArrayToHexString, "$1:").dropLast(1)
    }
}