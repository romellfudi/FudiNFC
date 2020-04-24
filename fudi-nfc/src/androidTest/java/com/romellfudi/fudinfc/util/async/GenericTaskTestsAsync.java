/*
 * Copyright (c) 2020. BoostTag E.I.R.L. Romell D.Z.
 * All rights reserved
 * porfile.romellfudi.com
 */

package com.romellfudi.fudinfc.util.async;

import android.content.Intent;
import android.nfc.FormatException;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.HandlerThread;
import android.util.Log;

import com.romellfudi.fudinfc.util.exceptions.InsufficientCapacityException;
import com.romellfudi.fudinfc.util.exceptions.ReadOnlyTagException;
import com.romellfudi.fudinfc.util.exceptions.TagNotPresentException;
import com.romellfudi.fudinfc.gear.interfaces.OpCallback;
import com.romellfudi.fudinfc.util.TestUtilities;
import com.romellfudi.fudinfc.util.interfaces.NfcWriteUtility;

/**
 * NfcLibrary by daneo
 * Created on 28/05/14.
 */
public class GenericTaskTestsAsync extends AbstractFailsTestsAsync {

    private static final String TAG = GenericTaskTestsAsync.class.getSimpleName();

    public GenericTaskTestsAsync() {
        mHandlerThread = new HandlerThread("Test") {
            @Override
            public void run() {
                if (mNfcToOperation != null) {
                    mNfcToOperation.executeWriteOperation();
                } else {
                    Log.d(TAG, "EmailAsync = null!");
                }
                release();
            }
        };
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testWriteFailedNdef() throws InterruptedException {
        performAsyncTaskWithReturnValue(getSucceedingAsyncUiCallback(), getFailingAsyncOperationCallback(), mTestUtilities.mockFailNdefWrite());
        assertFalse("Result was true!", checkResult("derp.com"));
    }

    public void testWriteFailedNdefFormatable() throws InterruptedException {
        performAsyncTaskWithReturnValue(getSucceedingAsyncUiCallback(), getFailingAsyncOperationCallback(), mTestUtilities.mockFailNdefWrite());
        assertFalse("Result was true!", checkResult("derp.com"));
    }


    public void testWriteSucceededNdef() throws InterruptedException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException {
        performAsyncTaskWithReturnValue(getSucceedingAsyncUiCallback(), getSucceedingAsyncOperationCallback(), mTestUtilities.mockNdefWriteUtility());
        assertTrue("Result was false!", checkResult("derp.com"));
    }

    public void testWriteSucceededNdefFormatable() throws InterruptedException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException {
        performAsyncTaskWithReturnValue(getSucceedingAsyncUiCallback(), getSucceedingAsyncOperationCallback(), mTestUtilities.mockNdefFormatableWrite());
        assertTrue("Result was false!", checkResult("derp.com"));
    }


    private OpCallback getSucceedingAsyncOperationCallback() throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException {
        final Tag mockTag = mTestUtilities.mockTag(TestUtilities.NDEF);
        final Intent intent = new Intent().putExtra(NfcAdapter.EXTRA_TAG, mockTag);
        return new OpCallback() {
            @Override
            public boolean performWrite(NfcWriteUtility writeUtility) throws ReadOnlyTagException, InsufficientCapacityException, TagNotPresentException, FormatException {
                return true;
            }
        };
    }

    private OpCallback getFailingAsyncOperationCallback() {
        return new OpCallback() {
            @Override
            public boolean performWrite(NfcWriteUtility writeUtility) throws ReadOnlyTagException, InsufficientCapacityException, TagNotPresentException, FormatException {
                return false;
            }
        };
    }


}
