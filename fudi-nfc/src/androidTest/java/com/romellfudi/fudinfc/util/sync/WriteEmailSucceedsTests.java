/*
 * WriteEmailSucceedsTests.java
 * NfcLibrary project.
 *
 * Created by : Daneo van Overloop - 17/6/2014.
 *
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 AppFoundry. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
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
