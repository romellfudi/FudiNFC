/*
 * Copyright (c) 2020. BoostTag E.I.R.L. Romell D.Z.
 * All rights reserved
 * porfile.romellfudi.com
 */
package com.romellfudi.fudinfc.util.interfaces

import android.nfc.FormatException
import android.nfc.NdefMessage
import android.nfc.Tag
import com.romellfudi.fudinfc.util.exceptions.InsufficientCapacityException
import com.romellfudi.fudinfc.util.exceptions.ReadOnlyTagException

interface WriteUtility : NdefWrite {
    /**
     * Writes towards a [android.nfc.Tag]
     *
     * @param message
     * to write
     * @param tag
     * to write to
     *
     * @return true if success
     *
     * @see .writeToNdef
     */
    fun writeSafelyToTag(message: NdefMessage?, tag: Tag?): Boolean

    /**
     * Write the given message to the tag
     *
     * @param message
     * to write
     * @param tag
     * to write to
     *
     * @return true if success
     *
     * @throws android.nfc.FormatException if the message is in an incorrect format
     * @throws com.romellfudi.fudinfc.util.exceptions.InsufficientCapacityException if there is not enough space available on the tag
     * @throws com.romellfudi.fudinfc.util.exceptions.ReadOnlyTagException when attempting to write to a read-only tag
     */
    @Throws(
        FormatException::class,
        ReadOnlyTagException::class,
        InsufficientCapacityException::class
    )
    fun writeToTag(message: NdefMessage?, tag: Tag?): Boolean

    /**
     * Used to mark the following operation as readonly
     * @return an instance of WriteUtility in order to chain
     */
    fun makeOperationReadOnly(): WriteUtility?
}