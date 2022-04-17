/*
 * Copyright (c) 2020. BoostTag E.I.R.L. Romell D.Z.
 * All rights reserved
 * porfile.romellfudi.com
 */
package com.romellfudi.fudinfc.util.sync

import android.nfc.FormatException
import android.nfc.NdefMessage
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.nfc.tech.NdefFormatable
import android.util.Log
import com.romellfudi.fudinfc.util.exceptions.InsufficientCapacityException
import com.romellfudi.fudinfc.util.exceptions.ReadOnlyTagException
import com.romellfudi.fudinfc.util.interfaces.NdefWrite
import com.romellfudi.fudinfc.util.interfaces.WriteUtility

class WriteUtilityImpl : WriteUtility {
    private var readOnly = false
    private var mNdefWrite: NdefWrite? = null

    constructor() {
        setNdefWrite(NdefWriteImpl())
    }

    /**
     * @param ndefWrite
     * used to delegate writing to Tag
     *
     * @throws java.lang.NullPointerException
     * when null
     */
    constructor(ndefWrite: NdefWrite?) {
        if (ndefWrite == null) {
            throw NullPointerException("WriteUtility cannot be null")
        }
        setNdefWrite(ndefWrite)
    }

    private fun setNdefWrite(ndefWrite: NdefWrite) {
        mNdefWrite = ndefWrite
    }

    @Throws(
        ReadOnlyTagException::class,
        InsufficientCapacityException::class,
        FormatException::class
    )
    override fun writeToNdef(message: NdefMessage?, ndef: Ndef?): Boolean {
        return mNdefWrite?.writeToNdef(message, ndef) == true
    }

    @Throws(
        ReadOnlyTagException::class,
        InsufficientCapacityException::class,
        FormatException::class
    )
    override fun writeToNdefAndMakeReadonly(message: NdefMessage?, ndef: Ndef?): Boolean {
        return mNdefWrite?.writeToNdefAndMakeReadonly(message, ndef) == true
    }

    @Throws(FormatException::class)
    override fun writeToNdefFormatable(
        message: NdefMessage?,
        ndefFormatable: NdefFormatable?
    ): Boolean {
        return mNdefWrite?.writeToNdefFormatable(message, ndefFormatable) == true
    }

    @Throws(FormatException::class)
    override fun writeToNdefFormatableAndMakeReadonly(
        message: NdefMessage?,
        ndefFormat: NdefFormatable?
    ): Boolean {
        return mNdefWrite?.writeToNdefFormatableAndMakeReadonly(message, ndefFormat) == true
    }

    override fun writeSafelyToTag(message: NdefMessage?, tag: Tag?): Boolean {
        try {
            writeToTag(message, tag)
        } catch (e: ReadOnlyTagException) {
            Log.d(TAG, "Tag is Read only !", e)
        } catch (e: InsufficientCapacityException) {
            Log.d(TAG, "The tag's capacity is insufficient!", e)
        } catch (e: FormatException) {
            Log.d(TAG, "The message is malformed!", e)
        }
        return false
    }

    @Throws(
        FormatException::class,
        ReadOnlyTagException::class,
        InsufficientCapacityException::class
    )
    override fun writeToTag(message: NdefMessage?, tag: Tag?): Boolean {
        val ndef = Ndef.get(tag)
        val formatable = NdefFormatable.get(tag)
        val result: Boolean = if (readOnly) {
            writeToNdefAndMakeReadonly(message, ndef) ||
                    writeToNdefFormatableAndMakeReadonly(message, formatable)
        } else {
            writeToNdef(message, ndef) || writeToNdefFormatable(message, formatable)
        }
        readOnly = false
        return result
    }

    override fun makeOperationReadOnly(): WriteUtility? {
        readOnly = true
        return this
    }

    companion object {
        private val TAG = WriteUtilityImpl::class.java.name
    }
}