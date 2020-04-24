/*
 * Copyright (c) 2020. BoostTag E.I.R.L. Romell D.Z.
 * All rights reserved
 * porfile.romellfudi.com
 */

package com.romellfudi.fudinfc.util.sync;

import android.content.Intent;
import android.nfc.FormatException;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.test.AndroidTestCase;

import com.romellfudi.fudinfc.util.constants.NfcHead;
import com.romellfudi.fudinfc.util.exceptions.InsufficientCapacityException;
import com.romellfudi.fudinfc.util.exceptions.ReadOnlyTagException;
import com.romellfudi.fudinfc.util.exceptions.TagNotPresentException;
import com.romellfudi.fudinfc.util.TestUtilities;
import com.romellfudi.fudinfc.util.interfaces.NfcWriteUtility;

/**
 * NfcLibrary by daneo
 * Created on 14/04/14.
 */
public class WritePhoneFailsTests extends AndroidTestCase {

    private TestUtilities mTestUtilities = new TestUtilities();

    // Telephone
    public void testWriteTelephoneNdef() throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException, InsufficientCapacityException, FormatException, ReadOnlyTagException, TagNotPresentException {
        String phoneNumber = "0123456789";
        final String ndef = TestUtilities.NDEF;
        performWriteAndChecks(phoneNumber, ndef, false);
    }

    public void testWriteTelephoneReadOnlyNdef() throws IllegalAccessException, FormatException, ClassNotFoundException, ReadOnlyTagException, InsufficientCapacityException, NoSuchFieldException, TagNotPresentException {
        String phoneNumber = "0123456789";
        final String ndef = TestUtilities.NDEF;
        performWriteAndChecks(phoneNumber, ndef,true);
    }

    public void testWriteTelephoneNdefFormatable() throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException, InsufficientCapacityException, FormatException, ReadOnlyTagException, TagNotPresentException {
        String phoneNumber = "0123456789";
        final String ndefFormatable = TestUtilities.NDEF_FORMATABLE;
        performWriteAndChecks(phoneNumber, ndefFormatable, false);
    }

    public void testWriteTelephoneReadOnlyNdefFormatable() throws IllegalAccessException, FormatException, ClassNotFoundException, ReadOnlyTagException, InsufficientCapacityException, NoSuchFieldException, TagNotPresentException {
        String phoneNumber = "0123456789";
        final String ndefFormatable = TestUtilities.NDEF_FORMATABLE;
        performWriteAndChecks(phoneNumber, ndefFormatable,true);
    }

    private void performWriteAndChecks(String phoneNumber, String ndefFormatable,boolean readonly) throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException, InsufficientCapacityException, FormatException, ReadOnlyTagException, TagNotPresentException {
        assertFalse(writePhoneNumber(phoneNumber, ndefFormatable, readonly));
        assertFalse(mTestUtilities.checkPayloadHeader(NfcHead.TEL));
    }

    boolean writePhoneNumber(String phoneNumber, String technology, boolean readonly) throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException, InsufficientCapacityException, FormatException, ReadOnlyTagException, TagNotPresentException {
        final Tag mockTag = mTestUtilities.mockTag(technology);

        final Intent intent = new Intent().putExtra(NfcAdapter.EXTRA_TAG,mockTag);
        NfcWriteUtility nfcMessageUtility = mTestUtilities.determineMockType(null);

        return nfcMessageUtility != null && (readonly ? nfcMessageUtility.makeOperationReadOnly().writeTelToTagFromIntent(phoneNumber, intent) : nfcMessageUtility.writeTelToTagFromIntent(phoneNumber, intent));
    }

}
