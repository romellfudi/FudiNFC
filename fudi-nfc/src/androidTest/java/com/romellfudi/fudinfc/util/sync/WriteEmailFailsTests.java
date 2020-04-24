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
 * Created on 28/05/14.
 */
public class WriteEmailFailsTests extends AndroidTestCase {

    private TestUtilities mTestUtilities = new TestUtilities();


    public void testWriteEmailFailedNdef() throws IllegalAccessException, FormatException, ClassNotFoundException, ReadOnlyTagException, InsufficientCapacityException, NoSuchFieldException, TagNotPresentException {
        String email = "derpherp.com";
        final String ndef = TestUtilities.NDEF;

        performWriteAndChecks(email, ndef,false);
    }



    public void testWriteEmailReadonlyFailedNdef() throws IllegalAccessException, FormatException, ClassNotFoundException, ReadOnlyTagException, InsufficientCapacityException, NoSuchFieldException, TagNotPresentException {
        String email = "derp@herp.com";
        final String ndef = TestUtilities.NDEF;

        performWriteAndChecks(email, ndef,true);
    }

    public void testWriteEmailFailNdefFormatable() throws IllegalAccessException, FormatException, ClassNotFoundException, ReadOnlyTagException, InsufficientCapacityException, NoSuchFieldException, TagNotPresentException {
        String email = "derp@herp.com";
        final String ndefFormatable = TestUtilities.NDEF_FORMATABLE;
        performWriteAndChecks(email, ndefFormatable,false);
    }

    public void testWriteEmailReadonlyFailNdefFormatable() throws IllegalAccessException, FormatException, ClassNotFoundException, ReadOnlyTagException, InsufficientCapacityException, NoSuchFieldException, TagNotPresentException {
        String email = "derp@herp.com";
        final String ndefFormatable = TestUtilities.NDEF_FORMATABLE;
        performWriteAndChecks(email, ndefFormatable,true);
    }
    private void performWriteAndChecks(String email, String ndef, boolean readonly) throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException, InsufficientCapacityException, FormatException, ReadOnlyTagException, TagNotPresentException {
        assertFalse(writeFailEmailToTag(email, ndef, readonly));
        assertFalse(mTestUtilities.checkResult(email));
        assertFalse(mTestUtilities.checkPayloadHeader(NfcHead.MAILTO));
    }

    boolean writeFailEmailToTag(String email, String technology, boolean readOnly) throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException, InsufficientCapacityException, FormatException, ReadOnlyTagException, TagNotPresentException {
        final Tag mockTag = mTestUtilities.mockTag(technology);
        final Intent intent = new Intent().putExtra(NfcAdapter.EXTRA_TAG, mockTag);
        NfcWriteUtility nfcWriteUtility = mTestUtilities.determineMockType(null);
        return readOnly ? nfcWriteUtility.makeOperationReadOnly().writeEmailToTagFromIntent(email, "", "", intent) : nfcWriteUtility.writeEmailToTagFromIntent(email, "", "", intent);
    }
}
