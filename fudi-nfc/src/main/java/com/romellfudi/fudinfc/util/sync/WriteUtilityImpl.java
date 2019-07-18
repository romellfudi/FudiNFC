package com.romellfudi.fudinfc.util.sync;

import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.util.Log;

import com.romellfudi.fudinfc.util.exceptions.InsufficientCapacityException;
import com.romellfudi.fudinfc.util.exceptions.ReadOnlyTagException;
import com.romellfudi.fudinfc.util.interfaces.NdefWrite;
import com.romellfudi.fudinfc.util.interfaces.WriteUtility;


public class WriteUtilityImpl implements WriteUtility {

    private static final String TAG = WriteUtilityImpl.class.getName();

    private boolean readOnly = false;

    private NdefWrite mNdefWrite;

    public WriteUtilityImpl() {
        setNdefWrite(new NdefWriteImpl());
    }

    /**
     * @param ndefWrite
     *         used to delegate writing to Tag
     *
     * @throws java.lang.NullPointerException
     *         when null
     */
    public WriteUtilityImpl(NdefWrite ndefWrite) {
        if (ndefWrite == null) {
            throw new NullPointerException("WriteUtility cannot be null");
        }
        setNdefWrite(ndefWrite);
    }

    private void setNdefWrite(NdefWrite ndefWrite) {
        this.mNdefWrite = ndefWrite;
    }

    @Override
    public boolean writeToNdef(NdefMessage message, Ndef ndef) throws ReadOnlyTagException, InsufficientCapacityException, FormatException {
        return mNdefWrite.writeToNdef(message, ndef);
    }

    @Override
    public boolean writeToNdefAndMakeReadonly(NdefMessage message, Ndef ndef) throws ReadOnlyTagException, InsufficientCapacityException, FormatException {
        return mNdefWrite.writeToNdefAndMakeReadonly(message, ndef);
    }

    @Override
    public boolean writeToNdefFormatable(NdefMessage message, NdefFormatable ndefFormatable) throws FormatException {
        return mNdefWrite.writeToNdefFormatable(message, ndefFormatable);
    }

    @Override
    public boolean writeToNdefFormatableAndMakeReadonly(NdefMessage message, NdefFormatable ndefFormat) throws FormatException {
        return mNdefWrite.writeToNdefFormatableAndMakeReadonly(message, ndefFormat);
    }

    @Override
    public boolean writeSafelyToTag(NdefMessage message, Tag tag) {
        try {
            writeToTag(message, tag);
        } catch (ReadOnlyTagException e) {
            Log.d(TAG, "Tag is Read only !", e);
        } catch (InsufficientCapacityException e) {
            Log.d(TAG, "The tag's capacity is insufficient!", e);
        } catch (FormatException e) {
            Log.d(TAG, "The message is malformed!", e);
        }

        return false;
    }

    @Override
    public boolean writeToTag(NdefMessage message, Tag tag) throws FormatException, ReadOnlyTagException, InsufficientCapacityException {
        Ndef ndef = Ndef.get(tag);
        NdefFormatable formatable = NdefFormatable.get(tag);

        boolean result;
        if (readOnly) {
            result = writeToNdefAndMakeReadonly(message, ndef) || writeToNdefFormatableAndMakeReadonly(message, formatable);
        } else {
            result = writeToNdef(message, ndef) || writeToNdefFormatable(message, formatable);
        }

        readOnly = false;
        return result;
    }

    public WriteUtility makeOperationReadOnly() {
        readOnly = true;
        return this;
    }
}
