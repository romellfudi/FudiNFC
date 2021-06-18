/*
 * Copyright (c) 2020. BoostTag E.I.R.L. Romell D.Z.
 * All rights reserved
 * porfile.romellfudi.com
 */
package com.romellfudi.fudinfc.util.interfaces

import android.content.Intent
import android.nfc.NdefMessage
import android.util.SparseArray

interface NfcReadUtility {
    /**
     * Read data from received [android.content.Intent]
     *
     * @param nfcDataIntent
     * the intent containing the data to read
     *
     * @return the data in an array of [android.content.Intent]'s or an empty [android.util.SparseArray] array
     */
    fun readFromTagWithSparseArray(nfcDataIntent: Intent?): SparseArray<String?>?

    /**
     * Read data from received [android.content.Intent]
     * @param nfcDataIntent the intent containing the data to read
     * @return the data is either a filled or empty [java.util.Map] depending on whether parsing was successful
     */
    fun readFromTagWithMap(nfcDataIntent: Intent?): Map<Byte?, String?>?

    /**
     * Retrieve the content type from the message
     * @param message type [NfcHead]
     * @return [NfcHead]
     */
    fun retrieveMessageTypes(message: NdefMessage?): Iterator<Byte?>?

    /**
     * Retrieve the actual message content
     * @param message to parse
     * @return the formatted message
     */
    fun retrieveMessage(message: NdefMessage?): String?
}