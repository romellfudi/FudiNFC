/*
 * Copyright (c) 2020. BoostTag E.I.R.L. Romell D.Z.
 * All rights reserved
 * porfile.romellfudi.com
 */
package com.romellfudi.fudinfc.util.sync

import android.content.Intent
import android.nfc.FormatException
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.nfc.Tag
import com.romellfudi.fudinfc.util.exceptions.InsufficientCapacityException
import com.romellfudi.fudinfc.util.exceptions.ReadOnlyTagException
import com.romellfudi.fudinfc.util.exceptions.TagNotPresentException
import com.romellfudi.fudinfc.util.interfaces.NfcMessageUtility
import com.romellfudi.fudinfc.util.interfaces.NfcWriteUtility
import com.romellfudi.fudinfc.util.interfaces.WriteUtility

class NfcWriteUtilityImpl : NfcWriteUtility {
    private var mNfcMessageUtility: NfcMessageUtility? = null
    private var mWriteUtility: WriteUtility? = null
    private var mReadOnly = false

    constructor() {
        initFields()
    }

    constructor(nfcMessageUtility: NfcMessageUtility?) {
        if (nfcMessageUtility == null) {
            throw NullPointerException("NfcMessageUtility cannot be null!")
        }
        setNfcMessageUtility(nfcMessageUtility)
        initFields()
    }

    constructor(writeUtility: WriteUtility?) {
        if (writeUtility == null) {
            throw NullPointerException("WriteUtility cannot be null!")
        }
        setWriteUtility(writeUtility)
        initFields()
    }

    /**
     * @param nfcMessageUtility used to create messages
     * @param writeUtility      used to write to tag
     * @throws java.lang.NullPointerException if either of the 2 arguments is null
     */
    constructor(nfcMessageUtility: NfcMessageUtility?, writeUtility: WriteUtility?) {
        if (writeUtility == null) {
            throw NullPointerException("WriteUtility cannot be null!")
        } else if (nfcMessageUtility == null) {
            throw NullPointerException("NfcMessageUtility cannot be null!")
        }
        setWriteUtility(writeUtility)
        setNfcMessageUtility(nfcMessageUtility)
    }

    private fun setNfcMessageUtility(nfcMessageUtility: NfcMessageUtility) {
        mNfcMessageUtility = nfcMessageUtility
    }

    private fun setWriteUtility(writeUtility: WriteUtility) {
        mWriteUtility = writeUtility
    }

    private fun initFields() {
        if (mNfcMessageUtility == null) {
            setNfcMessageUtility(NfcMessageUtilityImpl())
        }
        if (mWriteUtility == null) {
            setWriteUtility(WriteUtilityImpl())
        }
    }

    /**
     * {@inheritDoc}
     */
    @Throws(
        FormatException::class,
        ReadOnlyTagException::class,
        InsufficientCapacityException::class,
        TagNotPresentException::class
    )
    override fun writeUriToTagFromIntent(urlAddress: String, intent: Intent): Boolean {
        val ndefMessage = mNfcMessageUtility!!.createUri(urlAddress)
        val tag = retrieveTagFromIntent(intent)
        return writeToTag(ndefMessage, tag)
    }

    /**
     * {@inheritDoc}
     */
    @Throws(
        FormatException::class,
        ReadOnlyTagException::class,
        InsufficientCapacityException::class,
        TagNotPresentException::class
    )
    override fun writeUriWithPayloadToTagFromIntent(
        urlAddress: String,
        payloadHeader: Byte,
        intent: Intent
    ): Boolean {
        val ndefMessage = mNfcMessageUtility!!.createUri(urlAddress, payloadHeader)
        val tag = retrieveTagFromIntent(intent)
        return writeToTag(ndefMessage, tag)
    }

    /**
     * {@inheritDoc}
     */
    @Throws(
        FormatException::class,
        ReadOnlyTagException::class,
        InsufficientCapacityException::class,
        TagNotPresentException::class
    )
    override fun writeTelToTagFromIntent(telephone: String, intent: Intent): Boolean {
        val ndefMessage = mNfcMessageUtility!!.createTel(telephone)
        val tag = retrieveTagFromIntent(intent)
        return writeToTag(ndefMessage, tag)
    }

    /**
     * {@inheritDoc}
     */
    @Throws(
        FormatException::class,
        ReadOnlyTagException::class,
        InsufficientCapacityException::class,
        TagNotPresentException::class
    )
    override fun writeSmsToTagFromIntent(
        number: String,
        message: String?,
        intent: Intent
    ): Boolean {
        val ndefMessage = mNfcMessageUtility!!.createSms(number, message)
        val tag = retrieveTagFromIntent(intent)
        return mWriteUtility!!.writeToTag(ndefMessage, tag)
    }

    /**
     * {@inheritDoc}
     */
    @Throws(
        FormatException::class,
        ReadOnlyTagException::class,
        InsufficientCapacityException::class,
        TagNotPresentException::class
    )
    override fun writeGeolocationToTagFromIntent(
        latitude: Double?,
        longitude: Double?,
        intent: Intent
    ): Boolean {
        val ndefMessage = mNfcMessageUtility!!.createGeolocation(latitude, longitude)
        val tag = retrieveTagFromIntent(intent)
        return writeToTag(ndefMessage, tag)
    }

    /**
     * {@inheritDoc}
     */
    @Throws(
        FormatException::class,
        ReadOnlyTagException::class,
        InsufficientCapacityException::class,
        TagNotPresentException::class
    )
    override fun writeEmailToTagFromIntent(
        recipient: String,
        subject: String?,
        message: String?,
        intent: Intent
    ): Boolean {
        val ndefMessage = mNfcMessageUtility!!.createEmail(recipient, subject, message)
        val tag = retrieveTagFromIntent(intent)
        return writeToTag(ndefMessage, tag)
    }

    /**
     * {@inheritDoc}
     */
    @Throws(
        InsufficientCapacityException::class,
        FormatException::class,
        ReadOnlyTagException::class,
        TagNotPresentException::class
    )
    override fun writeBluetoothAddressToTagFromIntent(
        macAddress: String,
        intent: Intent?
    ): Boolean {
        val ndefMessage = mNfcMessageUtility!!.createBluetoothAddress(macAddress)
        val tag = retrieveTagFromIntent(intent)
        return writeToTag(ndefMessage, tag)
    }

    @Throws(
        FormatException::class,
        TagNotPresentException::class,
        ReadOnlyTagException::class,
        InsufficientCapacityException::class
    )
    override fun writeNdefMessageToTagFromIntent(message: NdefMessage, intent: Intent?): Boolean {
        val tag = retrieveTagFromIntent(intent)
        return writeToTag(message, tag)
    }

    @Throws(
        FormatException::class,
        TagNotPresentException::class,
        ReadOnlyTagException::class,
        InsufficientCapacityException::class
    )
    override fun writeTextToTagFromIntent(message: String, intent: Intent?): Boolean {
        val ndefMessage = mNfcMessageUtility!!.createText(message)
        val tag = retrieveTagFromIntent(intent)
        return writeToTag(ndefMessage, tag)
    }

    /**
     * {@inheritDoc}
     */
    override fun makeOperationReadOnly(): NfcWriteUtility? {
        mReadOnly = true
        return this
    }

    @Throws(
        FormatException::class,
        ReadOnlyTagException::class,
        InsufficientCapacityException::class
    )
    private fun writeToTag(ndefMessage: NdefMessage?, tag: Tag): Boolean {
        return if (mReadOnly) mWriteUtility!!.makeOperationReadOnly()!!
            .writeToTag(ndefMessage, tag) else mWriteUtility!!.writeToTag(ndefMessage, tag)
    }

    @Throws(TagNotPresentException::class)
    private fun retrieveTagFromIntent(i: Intent?): Tag {
        return i!!.getParcelableExtra(NfcAdapter.EXTRA_TAG)
            ?: throw TagNotPresentException()
    }
}