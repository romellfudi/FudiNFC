package com.romellfudi.fudinfc.util.sync;

import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.TagLostException;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.util.Log;

import java.io.IOException;

import com.romellfudi.fudinfc.util.exceptions.InsufficientCapacityException;
import com.romellfudi.fudinfc.util.exceptions.ReadOnlyTagException;
import com.romellfudi.fudinfc.util.interfaces.NdefWrite;

public class NdefWriteImpl implements NdefWrite {

    private static final String TAG = NdefWriteImpl.class.getName();

    private boolean mReadOnly = false;

    /**
     * Instantiates a new NdefWriteImpl.
     */
    public NdefWriteImpl() {

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean writeToNdef(NdefMessage message, Ndef ndef) throws ReadOnlyTagException, InsufficientCapacityException, FormatException {

        if (message == null || ndef == null) {
            return false;
        }

        int size = message.getByteArrayLength();

        try {

            ndef.connect();
            if (!ndef.isWritable()) {
                throw new ReadOnlyTagException();
            }
            if (ndef.getMaxSize() < size) {
                throw new InsufficientCapacityException();
            }
            ndef.writeNdefMessage(message);
            if (ndef.canMakeReadOnly() && mReadOnly) {
                ndef.makeReadOnly();
            } else if (mReadOnly) {
                throw new UnsupportedOperationException();
            }
            return true;
        } catch (IOException e) {
            Log.w(TAG, "IOException occurred", e);
        } finally {
            if (ndef.isConnected()) {
                try {
                    ndef.close();
                } catch (IOException e) {
                    Log.v(TAG, "IOException occurred at closing.", e);
                }
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean writeToNdefAndMakeReadonly(NdefMessage message, Ndef ndef) throws ReadOnlyTagException, InsufficientCapacityException, FormatException {
        setReadOnly(true);
        boolean result = writeToNdef(message, ndef);
        setReadOnly(false);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean writeToNdefFormatableAndMakeReadonly(NdefMessage message, NdefFormatable ndefFormat) throws FormatException {
        setReadOnly(true);
        boolean result = writeToNdefFormatable(message, ndefFormat);
        setReadOnly(false);

        return result;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean writeToNdefFormatable(NdefMessage message, NdefFormatable ndefFormatable) throws FormatException {
        if (ndefFormatable == null || message == null) {
            return false;
        }

        try {
            ndefFormatable.connect();
            if (mReadOnly) {
                ndefFormatable.formatReadOnly(message);
            } else {
                ndefFormatable.format(message);
            }

            return true;
        } catch (TagLostException e) {
            Log.d(TAG, "We lost our tag !", e);
        } catch (IOException e) {
            Log.w(TAG, "IOException occured", e);
        } catch (FormatException e) {
            Log.w(TAG, "Message is malformed occurred", e);
            throw e;
        } finally {
            if (ndefFormatable.isConnected()) {
                try {
                    ndefFormatable.close();
                } catch (IOException e) {
                    Log.w(TAG, "IOException occurred at closing.", e);
                }
            }
        }

        return false;
    }

    /**
     * Sets read only.
     *
     * @param readOnly the read only
     */
    void setReadOnly(boolean readOnly) {
        mReadOnly = readOnly;
    }
}