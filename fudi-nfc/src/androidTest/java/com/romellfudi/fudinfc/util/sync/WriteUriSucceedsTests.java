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
public class WriteUriSucceedsTests extends AndroidTestCase {

    private TestUtilities mTestUtilities = new TestUtilities();

    // Uri
    public void testWriteUriNdef() throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException, InsufficientCapacityException, FormatException, ReadOnlyTagException, TagNotPresentException {
        String uri = "http://www.derp.com";
        final String ndef = TestUtilities.NDEF;
        assertTrue(writeUri(uri, ndef, false));
        assertTrue(mTestUtilities.checkPayloadHeader(NfcHead.HTTP_WWW));
    }

    public void testWriteUriReadOnlyNdef() throws IllegalAccessException, FormatException, ClassNotFoundException, ReadOnlyTagException, InsufficientCapacityException, NoSuchFieldException, TagNotPresentException {
        String uri = "http://www.derp.com";
        final String ndef = TestUtilities.NDEF;
        performWriteAndChecks(uri, ndef);
    }

    public void testWriteUriNdefFormatable() throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException, InsufficientCapacityException, FormatException, ReadOnlyTagException, TagNotPresentException {
        String uri = "http://www.derp.com";
        final String ndefFormatable = TestUtilities.NDEF_FORMATABLE;
        assertTrue(writeUri(uri, ndefFormatable, false));
        assertTrue(mTestUtilities.checkPayloadHeader(NfcHead.HTTP_WWW));
    }

    public void testWriteUriReadOnlyNdefFormatable() throws IllegalAccessException, FormatException, ClassNotFoundException, ReadOnlyTagException, InsufficientCapacityException, NoSuchFieldException, TagNotPresentException {
        String uri = "http://www.derp.com";
        final String ndefFormatable = TestUtilities.NDEF_FORMATABLE;
        performWriteAndChecks(uri, ndefFormatable);
    }

    private void performWriteAndChecks(String uri, String ndefFormatable) throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException, InsufficientCapacityException, FormatException, ReadOnlyTagException, TagNotPresentException {
        assertTrue(writeUri(uri, ndefFormatable, true));
        assertTrue(mTestUtilities.checkPayloadHeader(NfcHead.HTTP_WWW));
    }

    public void testWriteUriFtpNdef() throws IllegalAccessException, FormatException, TagNotPresentException, ClassNotFoundException, ReadOnlyTagException, InsufficientCapacityException, NoSuchFieldException {
        String uri = "derp.com";
        final String ndef = TestUtilities.NDEF;
        assertTrue(writeUriCustomHeader(uri, ndef, NfcHead.HTTP,false));
    }

    boolean writeUri(String uri, String technology, boolean readonly) throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException, InsufficientCapacityException, FormatException, ReadOnlyTagException, TagNotPresentException {
        final Tag mockTag = mTestUtilities.mockTag(technology);
        final Intent intent = new Intent().putExtra(NfcAdapter.EXTRA_TAG, mockTag);
        NfcWriteUtility nfcWriteUtility = mTestUtilities.determineMockType(technology);

        return writeUriCustomHeader(uri,technology, NfcHead.HTTP_WWW,false);
    }

    boolean writeUriCustomHeader(String uri, String technology, byte header, boolean readonly) throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException, InsufficientCapacityException, FormatException, ReadOnlyTagException, TagNotPresentException {
        final Tag mockTag = mTestUtilities.mockTag(technology);
        final Intent intent = new Intent().putExtra(NfcAdapter.EXTRA_TAG, mockTag);
        NfcWriteUtility nfcWriteUtility = mTestUtilities.determineMockType(technology);

        return nfcWriteUtility != null && (readonly ? nfcWriteUtility.makeOperationReadOnly().writeUriWithPayloadToTagFromIntent(uri, header, intent) : nfcWriteUtility.writeUriWithPayloadToTagFromIntent(uri, header, intent));
    }
}
