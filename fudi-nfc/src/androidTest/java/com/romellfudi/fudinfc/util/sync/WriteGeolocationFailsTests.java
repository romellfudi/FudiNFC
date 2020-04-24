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
public class WriteGeolocationFailsTests extends AndroidTestCase {
    private TestUtilities mTestUtilities = new TestUtilities();

    // GeoLocation
    public void testWriteGeoLocationFailsNdef() throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException, InsufficientCapacityException, FormatException, ReadOnlyTagException, TagNotPresentException {
        double latitude = 0, longitude = 0;
        final String ndef = TestUtilities.NDEF;

        performWriteAndChecks(latitude, longitude, ndef,false);
    }

    public void testWriteGeoLocationReadOnlyNdef() throws IllegalAccessException, FormatException, ClassNotFoundException, ReadOnlyTagException, InsufficientCapacityException, NoSuchFieldException, TagNotPresentException {
        double latitude = 0, longitude = 0;
        final String ndef = TestUtilities.NDEF;
        performWriteAndChecks(latitude, longitude, ndef, true);
    }

    public void testWriteGeoLocationNdefFormatable() throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException, InsufficientCapacityException, FormatException, ReadOnlyTagException, TagNotPresentException {
        double latitude = 0, longitude = 0;
        final String ndefFormatable = TestUtilities.NDEF_FORMATABLE;
        performWriteAndChecks(latitude, longitude, ndefFormatable,false);
    }

    public void testWriteGeoLocationReadOnlyNdefFormatable() throws IllegalAccessException, FormatException, ClassNotFoundException, ReadOnlyTagException, InsufficientCapacityException, NoSuchFieldException, TagNotPresentException {
        double latitude = 0, longitude = 0;
        final String ndefFormatable = TestUtilities.NDEF_FORMATABLE;
        performWriteAndChecks(latitude, longitude, ndefFormatable, true);
    }

    private void performWriteAndChecks(double latitude, double longitude, String ndefFormatable,boolean readonly) throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException, InsufficientCapacityException, FormatException, ReadOnlyTagException, TagNotPresentException {
        assertFalse(writeGeoLocation(latitude, longitude, ndefFormatable, false));
        assertFalse(mTestUtilities.checkPayloadHeader(NfcHead.CUSTOM_SCHEME));
    }

    boolean writeGeoLocation(Double latitude, Double longitude, String technology, boolean readonly) throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException, InsufficientCapacityException, FormatException, ReadOnlyTagException, TagNotPresentException {
        final Tag mockTag = mTestUtilities.mockTag(technology);

        final Intent intent = new Intent().putExtra(NfcAdapter.EXTRA_TAG, mockTag);
        NfcWriteUtility nfcMessageUtility = mTestUtilities.determineMockType(null);

        return (readonly ? nfcMessageUtility.makeOperationReadOnly().writeGeolocationToTagFromIntent(latitude, longitude, intent) : nfcMessageUtility.writeGeolocationToTagFromIntent(latitude, longitude, intent));
    }
}
