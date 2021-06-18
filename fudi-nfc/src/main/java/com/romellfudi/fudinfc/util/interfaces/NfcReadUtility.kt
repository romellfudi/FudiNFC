/*
 * Copyright (c) 2020. BoostTag E.I.R.L. Romell D.Z.
 * All rights reserved
 * porfile.romellfudi.com
 */

package com.romellfudi.fudinfc.util.interfaces;

import android.content.Intent;
import android.nfc.NdefMessage;

import com.romellfudi.fudinfc.util.constants.NfcHead;

import java.util.Map;

public interface NfcReadUtility {

    /**
     * Read data from received {@link android.content.Intent}
     *
     * @param nfcDataIntent
     *         the intent containing the data to read
     *
     * @return the data in an array of {@link android.content.Intent}'s or an empty {@link android.util.SparseArray} array
     */
    android.util.SparseArray<String> readFromTagWithSparseArray(Intent nfcDataIntent);

    /**
     * Read data from received {@link android.content.Intent}
     * @param nfcDataIntent the intent containing the data to read
     * @return the data is either a filled or empty {@link java.util.Map} depending on whether parsing was successful
     */
    Map<Byte,String> readFromTagWithMap(Intent nfcDataIntent);


    /**
     * Retrieve the content type from the message
     * @param message type {@link NfcHead}
     * @return {@link NfcHead}
     */
    java.util.Iterator<Byte> retrieveMessageTypes(NdefMessage message);

    /**
     * Retrieve the actual message content
     * @param message to parse
     * @return the formatted message
     */
    String retrieveMessage(NdefMessage message);
}
