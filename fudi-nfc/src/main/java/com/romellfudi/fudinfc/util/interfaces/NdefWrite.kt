/*
 * Copyright (c) 2020. BoostTag E.I.R.L. Romell D.Z.
 * All rights reserved
 * porfile.romellfudi.com
 */
package com.romellfudi.fudinfc.util.interfaces

import android.nfc.FormatException
import android.nfc.NdefMessage
import android.nfc.tech.Ndef
import android.nfc.tech.NdefFormatable
import com.romellfudi.fudinfc.util.exceptions.InsufficientCapacityException
import com.romellfudi.fudinfc.util.exceptions.ReadOnlyTagException

interface NdefWrite {
    /**
     * Write message to ndef
     *
     * @param message
     * to write
     * @param ndef
     * from tag to write to
     *
     * @return true if success, false if ndef == null || message == null
     *
     * @throws ReadOnlyTagException
     * if tag is read-only
     * @throws InsufficientCapacityException
     * if the tag's capacity is not sufficient
     * @throws FormatException
     * if the message is malformed
     */
    @Throws(
        ReadOnlyTagException::class,
        InsufficientCapacityException::class,
        FormatException::class
    )
    fun writeToNdef(message: NdefMessage?, ndef: Ndef?): Boolean

    /**
     * Write message to ndef and make readonly
     *
     * @see NdefWrite.writeToNdef
     */
    @Throws(
        ReadOnlyTagException::class,
        InsufficientCapacityException::class,
        FormatException::class
    )
    fun writeToNdefAndMakeReadonly(message: NdefMessage?, ndef: Ndef?): Boolean

    /**
     * Write the message to an NdefFormatable
     * @param message to write
     * @param ndefFormatable to write to
     * @return true if success, false if ndefFormatable == null || message == null
     * @throws FormatException
     */
    @Throws(FormatException::class)
    fun writeToNdefFormatable(message: NdefMessage?, ndefFormatable: NdefFormatable?): Boolean

    /**
     * Write the message to an NdefFormatable and make readonly
     * @see NdefWrite.writeToNdefFormatable
     */
    @Throws(FormatException::class)
    fun writeToNdefFormatableAndMakeReadonly(
        message: NdefMessage?,
        ndefFormat: NdefFormatable?
    ): Boolean
}