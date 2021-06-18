/*
 * Copyright (c) 2020. BoostTag E.I.R.L. Romell D.Z.
 * All rights reserved
 * porfile.romellfudi.com
 */
package com.romellfudi.fudinfc.util.sync

import android.nfc.FormatException
import android.nfc.NdefMessage
import android.nfc.TagLostException
import android.nfc.tech.Ndef
import android.nfc.tech.NdefFormatable
import android.util.Log
import com.romellfudi.fudinfc.util.exceptions.InsufficientCapacityException
import com.romellfudi.fudinfc.util.exceptions.ReadOnlyTagException
import com.romellfudi.fudinfc.util.interfaces.NdefWrite
import java.io.IOException

class NdefWriteImpl
/**
 * Instantiates a new NdefWriteImpl.
 */
    : NdefWrite {
    private var mReadOnly = false

    /**
     * {@inheritDoc}
     */
    @Throws(
        ReadOnlyTagException::class,
        InsufficientCapacityException::class,
        FormatException::class
    )
    override fun writeToNdef(message: NdefMessage?, ndef: Ndef?): Boolean {
        if (message == null || ndef == null) {
            return false
        }
        val size = message.byteArrayLength
        try {
            ndef.connect()
            if (!ndef.isWritable) {
                throw ReadOnlyTagException()
            }
            if (ndef.maxSize < size) {
                throw InsufficientCapacityException()
            }
            ndef.writeNdefMessage(message)
            if (ndef.canMakeReadOnly() && mReadOnly) {
                ndef.makeReadOnly()
            } else if (mReadOnly) {
                throw UnsupportedOperationException()
            }
            return true
        } catch (e: IOException) {
            Log.w(TAG, "IOException occurred", e)
        } finally {
            if (ndef.isConnected) {
                try {
                    ndef.close()
                } catch (e: IOException) {
                    Log.v(TAG, "IOException occurred at closing.", e)
                }
            }
        }
        return false
    }

    /**
     * {@inheritDoc}
     */
    @Throws(
        ReadOnlyTagException::class,
        InsufficientCapacityException::class,
        FormatException::class
    )
    override fun writeToNdefAndMakeReadonly(message: NdefMessage?, ndef: Ndef?): Boolean {
        setReadOnly(true)
        val result = writeToNdef(message, ndef)
        setReadOnly(false)
        return result
    }

    /**
     * {@inheritDoc}
     */
    @Throws(FormatException::class)
    override fun writeToNdefFormatableAndMakeReadonly(
        message: NdefMessage?,
        ndefFormat: NdefFormatable?
    ): Boolean {
        setReadOnly(true)
        val result = writeToNdefFormatable(message, ndefFormat)
        setReadOnly(false)
        return result
    }

    /**
     * {@inheritDoc}
     */
    @Throws(FormatException::class)
    override fun writeToNdefFormatable(
        message: NdefMessage?,
        ndefFormatable: NdefFormatable?
    ): Boolean {
        if (ndefFormatable == null || message == null) {
            return false
        }
        try {
            ndefFormatable.connect()
            if (mReadOnly) {
                ndefFormatable.formatReadOnly(message)
            } else {
                ndefFormatable.format(message)
            }
            return true
        } catch (e: TagLostException) {
            Log.d(TAG, "We lost our tag !", e)
        } catch (e: IOException) {
            Log.w(TAG, "IOException occured", e)
        } catch (e: FormatException) {
            Log.w(TAG, "Message is malformed occurred", e)
            throw e
        } finally {
            if (ndefFormatable.isConnected) {
                try {
                    ndefFormatable.close()
                } catch (e: IOException) {
                    Log.w(TAG, "IOException occurred at closing.", e)
                }
            }
        }
        return false
    }

    /**
     * Sets read only.
     *
     * @param readOnly the read only
     */
    fun setReadOnly(readOnly: Boolean) {
        mReadOnly = readOnly
    }

    companion object {
        private val TAG = NdefWriteImpl::class.java.name
    }
}