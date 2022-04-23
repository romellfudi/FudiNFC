/*
 * Copyright (c) 2020. BoostTag E.I.R.L. Romell D.Z.
 * All rights reserved
 * porfile.romellfudi.com
 */
package com.romellfudi.fudinfc.gear

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.nfc.NfcAdapter.CreateNdefMessageCallback
import android.nfc.NfcEvent
import android.nfc.tech.Ndef
import android.nfc.tech.NdefFormatable
import android.os.Bundle
import android.util.Log
import android.util.SparseArray
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.forEach
import com.romellfudi.fudinfc.util.interfaces.NfcMessageUtility
import com.romellfudi.fudinfc.util.interfaces.NfcReadUtility
import com.romellfudi.fudinfc.util.sync.NfcMessageUtilityImpl
import com.romellfudi.fudinfc.util.sync.NfcReadUtilityImpl
import java.util.*

abstract class NfcAct : AppCompatActivity(), CreateNdefMessageCallback {
    protected var nfcAdapter: NfcAdapter? = null
    protected var nfcMessages: List<String?>? = null
        private set
    val nfcMessageUtility: NfcMessageUtility = NfcMessageUtilityImpl()
    val nfcReadUtility: NfcReadUtility = NfcReadUtilityImpl()
    private var pendingIntent: PendingIntent? = null
    private lateinit var mIntentFilters: Array<IntentFilter>
    private lateinit var mTechLists: Array<Array<String>>
    private var mBeamEnabled = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initAdapter()
        initFields()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        if (savedInstanceState.getBoolean(beamEnabled)) {
            enableBeam()
        } else {
            disableBeam()
        }
    }

    public override fun onResume() {
        super.onResume()
        initAdapter()
        if (nfcAdapter != null) {
            nfcAdapter?.enableForegroundDispatch(this, pendingIntent, mIntentFilters, mTechLists)
            Log.d(TAG, "FGD enabled")
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        Log.d(TAG, "Received intent!")
        setIntent(intent)
        if (getIntent() != null) {
            if (NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action || NfcAdapter.ACTION_TECH_DISCOVERED == intent.action) {
                nfcMessages =
                    transformSparseArrayToArrayList(nfcReadUtility.readFromTagWithSparseArray(intent))
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(beamEnabled, mBeamEnabled)
    }

    override fun onPause() {
        super.onPause()
        if (nfcAdapter != null) {
            nfcAdapter?.disableForegroundDispatch(this)
            Log.d(TAG, "FGD disabled")
        }
    }

    override fun createNdefMessage(event: NfcEvent): NdefMessage {
        return NfcMessageUtilityImpl().createText("You're seeing this message because you have not overridden the createNdefMessage(NfcEvent event) in your activity.")
    }

    private fun initFields() {
        pendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
            PendingIntent.FLAG_MUTABLE
        )
        mIntentFilters = arrayOf(IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED))
        mTechLists = arrayOf(
            arrayOf(Ndef::class.java.name), arrayOf(
                NdefFormatable::class.java.name
            )
        )
    }

    private fun initAdapter() {
        if (nfcAdapter == null) {
            nfcAdapter = NfcAdapter.getDefaultAdapter(this)
            Log.d(TAG, "Adapter initialized")
        }
    }

    protected fun enableBeam() {
        if (nfcAdapter != null) {
            nfcAdapter?.setNdefPushMessageCallback(this, this)
            mBeamEnabled = true
            Log.d(TAG, "Beam enabled")
        }
    }

    protected fun disableBeam() {
        if (nfcAdapter != null) {
            nfcAdapter?.setNdefPushMessageCallback(null, this)
            mBeamEnabled = false
            Log.d(TAG, "Beam disabled")
        }
    }

    protected fun transformSparseArrayToArrayList(sparseArray: SparseArray<String?>?) =
        ArrayList<String?>(sparseArray?.size() ?: 0).apply {
            sparseArray?.forEach { _, value -> add(value) }
        }

    companion object {
        private val TAG = NfcAct::class.java.name
        private const val beamEnabled = "androidBeamEnabled"
    }
}