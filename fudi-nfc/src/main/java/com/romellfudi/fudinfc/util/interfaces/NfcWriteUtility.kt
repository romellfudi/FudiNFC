/*
 * Copyright (c) 2020. BoostTag E.I.R.L. Romell D.Z.
 * All rights reserved
 * porfile.romellfudi.com
 */
package com.romellfudi.fudinfc.util.interfaces

import android.content.Intent
import android.nfc.FormatException
import android.nfc.NdefMessage
import com.romellfudi.fudinfc.util.exceptions.InsufficientCapacityException
import com.romellfudi.fudinfc.util.exceptions.ReadOnlyTagException
import com.romellfudi.fudinfc.util.exceptions.TagNotPresentException

interface NfcWriteUtility {
    /**
     * @param urlAddress
     * The url, do not put in any prefix, [NfcHead.HTTP_WWW] is auto added.
     * @param intent
     * to write to
     *
     * @return true if successful
     */
    @Throws(
        FormatException::class,
        ReadOnlyTagException::class,
        InsufficientCapacityException::class,
        TagNotPresentException::class
    )
    fun writeUriToTagFromIntent(urlAddress: String, intent: Intent): Boolean

    /**
     * @param urlAddress
     * The url, do not put in any prefix, [NfcHead.HTTP_WWW] is auto added.
     * @param intent
     * to write to
     *
     * @return true if successful
     */
    @Throws(
        FormatException::class,
        ReadOnlyTagException::class,
        InsufficientCapacityException::class,
        TagNotPresentException::class
    )
    fun writeUriWithPayloadToTagFromIntent(
        urlAddress: String,
        payloadHeader: Byte,
        intent: Intent
    ): Boolean

    /**
     * Writes a telephone number to the tag
     *
     * @param telephone
     * number to write
     * @param intent
     * to write to
     * e to
     *
     * @return true if success
     */
    @Throws(
        FormatException::class,
        ReadOnlyTagException::class,
        InsufficientCapacityException::class,
        TagNotPresentException::class
    )
    fun writeTelToTagFromIntent(telephone: String, intent: Intent): Boolean

    /**
     * Write SMS to tag. Due to a bug in Android this is not correctly implemented by the OS.
     *
     * @param number
     * of the recipient
     * @param message
     * to send to the person
     * @param intent
     * to write to
     *
     * @return true if success
     */
    @Throws(
        FormatException::class,
        ReadOnlyTagException::class,
        InsufficientCapacityException::class,
        TagNotPresentException::class
    )
    fun writeSmsToTagFromIntent(number: String, message: String?, intent: Intent): Boolean

    /**
     * @param latitude
     * maximum 6 decimals
     * @param longitude
     * maximum 6 DECIMALS
     * @param intent
     * to to write to
     *
     * @return true if success
     */
    @Throws(
        FormatException::class,
        ReadOnlyTagException::class,
        InsufficientCapacityException::class,
        TagNotPresentException::class
    )
    fun writeGeolocationToTagFromIntent(
        latitude: Double?,
        longitude: Double?,
        intent: Intent
    ): Boolean

    /**
     * Write recipient, subject and message to tag
     *
     * @param recipient
     * to whom the mail should be sent
     * @param subject
     * of the email
     * @param message
     * body of the email
     * @param intent
     * to write to
     *
     * @return true if success
     */
    @Throws(
        FormatException::class,
        ReadOnlyTagException::class,
        InsufficientCapacityException::class,
        TagNotPresentException::class
    )
    fun writeEmailToTagFromIntent(
        recipient: String,
        subject: String?,
        message: String?,
        intent: Intent
    ): Boolean

    /**
     * Write the bluetooth address to the tag
     *
     * @param macAddress
     * to write to the tag. Must be in format XX:XX:XX:XX:XX:XX, separator may differ
     * @param intent
     * to write to
     *
     * @return true if success
     */
    @Throws(
        InsufficientCapacityException::class,
        FormatException::class,
        ReadOnlyTagException::class,
        TagNotPresentException::class
    )
    fun writeBluetoothAddressToTagFromIntent(macAddress: String, intent: Intent?): Boolean

    /**
     * Pass a raw NdefMessage along to write
     * @param message to write to the tag
     * @param intent to write to
     * @return true if success
     * @throws FormatException
     */
    @Throws(
        FormatException::class,
        TagNotPresentException::class,
        ReadOnlyTagException::class,
        InsufficientCapacityException::class
    )
    fun writeNdefMessageToTagFromIntent(message: NdefMessage, intent: Intent?): Boolean

    /**
     * Pass a raw NdefMessage along to write
     * @param message to write to the tag
     * @param intent to write to
     * @return true if success
     * @throws FormatException
     */
    @Throws(
        FormatException::class,
        TagNotPresentException::class,
        ReadOnlyTagException::class,
        InsufficientCapacityException::class
    )
    fun writeTextToTagFromIntent(message: String, intent: Intent?): Boolean

    /**
     * Used to mark the following operation as readonly
     *
     * @return an instance of WriteUtility in order to chain
     */
    fun makeOperationReadOnly(): NfcWriteUtility?
}