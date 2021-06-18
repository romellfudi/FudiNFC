/*
 * Copyright (c) 2020. BoostTag E.I.R.L. Romell D.Z.
 * All rights reserved
 * porfile.romellfudi.com
 */

package com.romellfudi.fudinfc.util.sync;

import android.content.Intent;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;

import com.romellfudi.fudinfc.util.exceptions.InsufficientCapacityException;
import com.romellfudi.fudinfc.util.exceptions.ReadOnlyTagException;
import com.romellfudi.fudinfc.util.exceptions.TagNotPresentException;
import com.romellfudi.fudinfc.util.interfaces.NfcMessageUtility;
import com.romellfudi.fudinfc.util.interfaces.NfcWriteUtility;
import com.romellfudi.fudinfc.util.interfaces.WriteUtility;

import org.jetbrains.annotations.NotNull;

public class NfcWriteUtilityImpl implements NfcWriteUtility {

    private NfcMessageUtility mNfcMessageUtility;
    private WriteUtility mWriteUtility;

    private boolean mReadOnly;

    public NfcWriteUtilityImpl() {
        initFields();
    }

    public NfcWriteUtilityImpl(NfcMessageUtility nfcMessageUtility) {
        if (nfcMessageUtility == null) {
            throw new NullPointerException("NfcMessageUtility cannot be null!");
        }
        setNfcMessageUtility(nfcMessageUtility);
        initFields();
    }

    public NfcWriteUtilityImpl(WriteUtility writeUtility) {
        if (writeUtility == null) {
            throw new NullPointerException("WriteUtility cannot be null!");
        }
        setWriteUtility(writeUtility);
        initFields();
    }

    /**
     * @param nfcMessageUtility used to create messages
     * @param writeUtility      used to write to tag
     * @throws java.lang.NullPointerException if either of the 2 arguments is null
     */
    public NfcWriteUtilityImpl(NfcMessageUtility nfcMessageUtility, WriteUtility writeUtility) {

        if (writeUtility == null) {
            throw new NullPointerException("WriteUtility cannot be null!");
        } else if (nfcMessageUtility == null) {
            throw new NullPointerException("NfcMessageUtility cannot be null!");
        }
        setWriteUtility(writeUtility);
        setNfcMessageUtility(nfcMessageUtility);
    }

    private void setNfcMessageUtility(NfcMessageUtility nfcMessageUtility) {
        mNfcMessageUtility = nfcMessageUtility;
    }

    private void setWriteUtility(WriteUtility writeUtility) {
        mWriteUtility = writeUtility;
    }

    private void initFields() {
        if (mNfcMessageUtility == null) {
            setNfcMessageUtility(new NfcMessageUtilityImpl());
        }
        if (mWriteUtility == null) {
            setWriteUtility(new WriteUtilityImpl());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean writeUriToTagFromIntent(@NotNull String urlAddress, @NotNull Intent intent) throws FormatException, ReadOnlyTagException, InsufficientCapacityException, TagNotPresentException {
        NdefMessage ndefMessage = mNfcMessageUtility.createUri(urlAddress);
        final Tag tag = retrieveTagFromIntent(intent);
        return writeToTag(ndefMessage, tag);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean writeUriWithPayloadToTagFromIntent(@NotNull String urlAddress, byte payloadHeader, @NotNull Intent intent) throws FormatException, ReadOnlyTagException, InsufficientCapacityException, TagNotPresentException {
        NdefMessage ndefMessage = mNfcMessageUtility.createUri(urlAddress, payloadHeader);
        final Tag tag = retrieveTagFromIntent(intent);
        return writeToTag(ndefMessage, tag);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean writeTelToTagFromIntent(@NotNull String telephone, @NotNull Intent intent) throws FormatException, ReadOnlyTagException, InsufficientCapacityException, TagNotPresentException {
        final NdefMessage ndefMessage = mNfcMessageUtility.createTel(telephone);
        final Tag tag = retrieveTagFromIntent(intent);
        return writeToTag(ndefMessage, tag);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean writeSmsToTagFromIntent(@NotNull String number, String message, @NotNull Intent intent) throws FormatException, ReadOnlyTagException, InsufficientCapacityException, TagNotPresentException {
        final NdefMessage ndefMessage = mNfcMessageUtility.createSms(number, message);
        final Tag tag = retrieveTagFromIntent(intent);
        return mWriteUtility.writeToTag(ndefMessage, tag);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean writeGeolocationToTagFromIntent(Double latitude, Double longitude, @NotNull Intent intent) throws FormatException, ReadOnlyTagException, InsufficientCapacityException, TagNotPresentException {
        final NdefMessage ndefMessage = mNfcMessageUtility.createGeolocation(latitude, longitude);
        final Tag tag = retrieveTagFromIntent(intent);
        return writeToTag(ndefMessage, tag);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean writeEmailToTagFromIntent(@NotNull String recipient, String subject, String message, @NotNull Intent intent) throws FormatException, ReadOnlyTagException, InsufficientCapacityException, TagNotPresentException {
        final NdefMessage ndefMessage = mNfcMessageUtility.createEmail(recipient, subject, message);
        final Tag tag = retrieveTagFromIntent(intent);
        return writeToTag(ndefMessage, tag);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean writeBluetoothAddressToTagFromIntent(@NotNull String macAddress, Intent intent) throws InsufficientCapacityException, FormatException, ReadOnlyTagException, TagNotPresentException {
        final NdefMessage ndefMessage = mNfcMessageUtility.createBluetoothAddress(macAddress);
        final Tag tag = retrieveTagFromIntent(intent);
        return writeToTag(ndefMessage, tag);
    }

    @Override
    public boolean writeNdefMessageToTagFromIntent(@NotNull NdefMessage message, Intent intent) throws FormatException, TagNotPresentException, ReadOnlyTagException, InsufficientCapacityException {
        final Tag tag = retrieveTagFromIntent(intent);
        return writeToTag(message, tag);
    }

    @Override
    public boolean writeTextToTagFromIntent(@NotNull String message, Intent intent) throws FormatException, TagNotPresentException, ReadOnlyTagException, InsufficientCapacityException {
        final NdefMessage ndefMessage = mNfcMessageUtility.createText(message);
        final Tag tag = retrieveTagFromIntent(intent);
        return writeToTag(ndefMessage, tag);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NfcWriteUtility makeOperationReadOnly() {
        mReadOnly = true;
        return this;
    }

    private boolean writeToTag(NdefMessage ndefMessage, Tag tag) throws FormatException, ReadOnlyTagException, InsufficientCapacityException {
        return mReadOnly ? mWriteUtility.makeOperationReadOnly().writeToTag(ndefMessage, tag) : mWriteUtility.writeToTag(ndefMessage, tag);
    }

    @NotNull
    private Tag retrieveTagFromIntent(Intent i) throws TagNotPresentException {
        Tag tag = i.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (tag == null) {
            throw new TagNotPresentException();
        }
        return tag;
    }
}
