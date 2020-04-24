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
 * Created on 28/03/14.
 */
public class WriteEmailSucceedsTests extends AndroidTestCase {

    private static final String TAG = "be.idamediafoundry.nfclibrary.implementationapp.NFCActivity";

    TestUtilities mTestUtilities = new TestUtilities();

    public WriteEmailSucceedsTests() {
        //super(NFCActivity.class);
    }


    /**
     * Sets up the tag for each test case
     *
     * @throws Exception
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();

    }

    // Email
    public void testWriteEmailNdef() throws IllegalAccessException, FormatException, ClassNotFoundException, ReadOnlyTagException, InsufficientCapacityException, NoSuchFieldException, TagNotPresentException {
        String email = "derp@herp.com";
        assertTrue(writeEmailToTag(email, TestUtilities.NDEF, false));

        assertTrue(mTestUtilities.checkResult(email));
        assertTrue(mTestUtilities.checkPayloadHeader(NfcHead.MAILTO));
    }


    public void testWriteEmailReadonlyNdef() throws IllegalAccessException, FormatException, ClassNotFoundException, ReadOnlyTagException, InsufficientCapacityException, NoSuchFieldException, TagNotPresentException {
        String email = "derp@herp.com";
        assertTrue(writeEmailToTag(email, TestUtilities.NDEF, true));

        assertTrue(mTestUtilities.checkResult(email));
        assertTrue(mTestUtilities.checkPayloadHeader(NfcHead.MAILTO));
    }


    public void testWriteEmailNdefFormatable() throws IllegalAccessException, FormatException, ClassNotFoundException, ReadOnlyTagException, InsufficientCapacityException, NoSuchFieldException, TagNotPresentException {
        String email = "derp@herp.com";
        assertTrue(writeEmailToTag(email, TestUtilities.NDEF_FORMATABLE, false));

        assertTrue(mTestUtilities.checkResult(email));
        assertTrue(mTestUtilities.checkPayloadHeader(NfcHead.MAILTO));
    }

    public void testWriteEmailReadonlyNdefFormatable() throws IllegalAccessException, FormatException, ClassNotFoundException, ReadOnlyTagException, InsufficientCapacityException, NoSuchFieldException, TagNotPresentException {
        String email = "derp@herp.com";
        assertTrue(writeEmailToTag(email, TestUtilities.NDEF_FORMATABLE, true));

        assertTrue(mTestUtilities.checkResult(email));
        assertTrue(mTestUtilities.checkPayloadHeader(NfcHead.MAILTO));
    }

    boolean writeEmailToTag(String email, String technology, boolean readOnly) throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException, InsufficientCapacityException, FormatException, ReadOnlyTagException, TagNotPresentException {
        final Tag mockTag = mTestUtilities.mockTag(technology);
        final Intent intent = new Intent().putExtra(NfcAdapter.EXTRA_TAG, mockTag);
        NfcWriteUtility nfcWriteUtility = mTestUtilities.determineMockType(technology);
        return nfcWriteUtility != null && (readOnly ? nfcWriteUtility.makeOperationReadOnly().writeEmailToTagFromIntent(email, "", "", intent) : nfcWriteUtility.writeEmailToTagFromIntent(email, "", "", intent));
    }


}
