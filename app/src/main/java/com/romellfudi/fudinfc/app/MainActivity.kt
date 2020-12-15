/*
 * Copyright (c) 2020. BoostTag E.I.R.L. Romell D.Z.
 * All rights reserved
 * porfile.romellfudi.com
 */
package com.romellfudi.fudinfc.app

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import com.rbddevs.splashy.Splashy
import com.romellfudi.fudinfc.app.di.component.DaggerNFCComponent
import com.romellfudi.fudinfc.gear.NfcAct
import com.romellfudi.fudinfc.gear.interfaces.OpCallback
import com.romellfudi.fudinfc.gear.interfaces.TaskCallback
import com.romellfudi.fudinfc.util.async.WriteCallbackNfc
import com.romellfudi.fudinfc.util.interfaces.NfcReadUtility
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : NfcAct() {

    @Inject
    lateinit var mProgressDialog: ProgressDialog

    @Inject
    lateinit var splashy: Splashy

    @Inject
    lateinit var mNfcReadUtility: NfcReadUtility

    @Inject
    lateinit var mTaskCallback: TaskCallback

    var mOpCallback: OpCallback? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        DaggerNFCComponent.factory().create(this).inject(this)
        splashy.show()
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
        if (mOpCallback != null && mProgressDialog != null && mProgressDialog!!.isShowing) {
            WriteCallbackNfc(mTaskCallback, mOpCallback!!).executeWriteOperation()
            mOpCallback = null
        } else {
            for (data in mNfcReadUtility.readFromTagWithMap(paramIntent).values)
                Toast.makeText(this, data, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dismissDialog()
    }

    private fun showDialog() {
        progressbar.visibility = View.VISIBLE
        Handler().postDelayed({ progressbar.visibility = View.INVISIBLE }, 10000)
        mProgressDialog.setTitle(R.string.progressdialog_waiting_for_tag)
        mProgressDialog.setMessage(getString(R.string.progressdialog_waiting_for_tag_message))
        mProgressDialog.show()
    }

    private fun dismissDialog() {
        mProgressDialog.dismiss()
        progressbar.visibility = View.INVISIBLE
    }
}