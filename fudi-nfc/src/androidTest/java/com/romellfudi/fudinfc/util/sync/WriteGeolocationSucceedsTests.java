/*
 * WriteGeolocationSucceedsTests.java
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
 * Created on 14/04/14.
 */
public class WriteGeolocationSucceedsTests extends AndroidTestCase {
    private TestUtilities mTestUtilities = new TestUtilities();

    // GeoLocation
    public void testWriteGeoLocationNdef() throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException, InsufficientCapacityException, FormatException, ReadOnlyTagException, TagNotPresentException {
        double latitude = 0, longitude = 0;
        final String ndef = TestUtilities.NDEF;

        performWriteAndChecks(latitude, longitude, ndef, false);
    }

    public void testWriteGeoLocationReadOnlyNdef() throws IllegalAccessException, FormatException, ClassNotFoundException, ReadOnlyTagException, InsufficientCapacityException, NoSuchFieldException, TagNotPresentException {
        double latitude = 0, longitude = 0;
        final String ndef = TestUtilities.NDEF;
        performWriteAndChecks(latitude, longitude, ndef,true);
    }

    public void testWriteGeoLocationNdefFormatable() throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException, InsufficientCapacityException, FormatException, ReadOnlyTagException, TagNotPresentException {
        double latitude = 0, longitude = 0;
        final String ndefFormatable = TestUtilities.NDEF_FORMATABLE;
        performWriteAndChecks(latitude, longitude, ndefFormatable, false);
    }

    public void testWriteGeoLocationReadOnlyNdefFormatable() throws IllegalAccessException, FormatException, ClassNotFoundException, ReadOnlyTagException, InsufficientCapacityException, NoSuchFieldException, TagNotPresentException {
        double latitude = 0, longitude = 0;
        final String ndefFormatable = TestUtilities.NDEF_FORMATABLE;
        performWriteAndChecks(latitude, longitude, ndefFormatable,true);
    }

    private void performWriteAndChecks(double latitude, double longitude, String ndef,boolean readonly) throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException, InsufficientCapacityException, FormatException, ReadOnlyTagException, TagNotPresentException {
        assertTrue(writeGeoLocation(latitude, longitude, ndef, readonly));
        assertTrue(mTestUtilities.checkPayloadHeader(NfcHead.CUSTOM_SCHEME));
    }

    boolean writeGeoLocation(Double latitude, Double longitude, String technology, boolean readonly) throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException, InsufficientCapacityException, FormatException, ReadOnlyTagException, TagNotPresentException {
        final Tag mockTag = mTestUtilities.mockTag(technology);

        final Intent intent = new Intent().putExtra(NfcAdapter.EXTRA_TAG, mockTag);
        NfcWriteUtility nfcMessageUtility = mTestUtilities.determineMockType(technology);

        return (readonly ? nfcMessageUtility.makeOperationReadOnly().writeGeolocationToTagFromIntent(latitude, longitude, intent) : nfcMessageUtility.writeGeolocationToTagFromIntent(latitude, longitude, intent));
    }
}
