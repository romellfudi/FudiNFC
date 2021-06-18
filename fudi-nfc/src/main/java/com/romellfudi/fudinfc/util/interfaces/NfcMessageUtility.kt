/*
 * Copyright (c) 2020. BoostTag E.I.R.L. Romell D.Z.
 * All rights reserved
 * porfile.romellfudi.com
 */
package com.romellfudi.fudinfc.util.interfaces

import android.nfc.FormatException
import android.nfc.NdefMessage
import com.romellfudi.fudinfc.util.exceptions.InsufficientCapacityException
import com.romellfudi.fudinfc.util.exceptions.ReadOnlyTagException

interface NfcMessageUtility {
    /**
     * @param urlAddress
     * The url, auto-prefixed with the http://www. header
     * @return true if successful
     */
    @Throws(FormatException::class)
    fun createUri(urlAddress: String): NdefMessage?

    /**
     * Creates a telephone number - NdefMessage
     *
     * @param telephone
     * number to create
     * @return true if success
     */
    @Throws(FormatException::class)
    fun createTel(telephone: String): NdefMessage?

    /**
     * Create SMS - NdefMessage. Due to a bug in Android this is not correctly implemented by the OS.
     *
     * @param message
     * to send to the person
     * @param number
     * of the recipient
     * @return true if success
     */
    @Throws(FormatException::class)
    fun createSms(number: String, message: String?): NdefMessage?

    /**
     * Creates a Geolocation - NdefMessage.
     * @param latitude
     * maximum 6 decimals
     * @param longitude
     * maximum 6 DECIMALS
     * @return true if success
     */
    @Throws(FormatException::class)
    fun createGeolocation(latitude: Double?, longitude: Double?): NdefMessage?

    /**
     * Create recipient, subject and message email - NdefMessage
     *
     * @param recipient
     * to whom the mail should be sent
     * @param subject
     * of the email
     * @param message
     * body of the email
     * @return true if success
     */
    @Throws(FormatException::class)
    fun createEmail(recipient: String, subject: String?, message: String?): NdefMessage?

    /**
     * Create the bluetooth address - NdefMessage
     *
     * @param macAddress
     * to create NdefMessage. Must be in format XX:XX:XX:XX:XX:XX, separator may differ
     * @return true if success
     */
    @Throws(
        InsufficientCapacityException::class,
        FormatException::class,
        ReadOnlyTagException::class
    )
    fun createBluetoothAddress(macAddress: String): NdefMessage?

    /**
     * Create the message with plain text
     * @param text to transfer
     * @return prepared NdefMessage
     * @throws FormatException
     */
    @Throws(FormatException::class)
    fun createText(text: String): NdefMessage?

    /**
     * Create the address with the given header
     * @see .createUri
     */
    @Throws(FormatException::class)
    fun createUri(urlAddress: String?, payloadHeader: Byte): NdefMessage?
}