/*
 * AbstractFailsTestsAsync.java
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

package com.romellfudi.fudinfc.util.async;

import android.nfc.FormatException;
import android.os.HandlerThread;
import android.test.InstrumentationTestCase;

import java.util.concurrent.Semaphore;

import com.romellfudi.fudinfc.util.exceptions.InsufficientCapacityException;
import com.romellfudi.fudinfc.util.exceptions.ReadOnlyTagException;
import com.romellfudi.fudinfc.util.exceptions.TagNotPresentException;
import com.romellfudi.fudinfc.gear.interfaces.OpCallback;
import com.romellfudi.fudinfc.gear.interfaces.TaskCallback;
import com.romellfudi.fudinfc.util.TestUtilities;
import com.romellfudi.fudinfc.util.interfaces.NfcToOperation;
import com.romellfudi.fudinfc.util.interfaces.NfcWriteUtility;

/**
 * NfcLibrary by daneo
 * Created on 30/05/14.
 */
public class AbstractFailsTestsAsync extends InstrumentationTestCase {
    protected HandlerThread mHandlerThread;
    protected TestUtilities mTestUtilities = new TestUtilities();
    protected NfcToOperation mNfcToOperation;
    protected Semaphore mSemaphore;

    protected void performAsyncTaskWithReturnValue(TaskCallback taskCallback, NfcWriteUtility nfcWriteUtility, final boolean returnValue) throws InterruptedException {
        mSemaphore = new Semaphore(0);

        this.mNfcToOperation = new WriteEmailNfc(taskCallback,
                new OpCallback() {
                    @Override
                    public boolean performWrite(NfcWriteUtility writeUtility) throws ReadOnlyTagException, InsufficientCapacityException, TagNotPresentException, FormatException {
                        return returnValue;
                    }
                }
                ,
                nfcWriteUtility
        );

        mHandlerThread.start();
        mSemaphore.acquire();
    }

    protected void performAsyncTaskWithReturnValue(TaskCallback taskCallback, OpCallback opCallback, NfcWriteUtility nfcWriteUtility) throws InterruptedException {
        mSemaphore = new Semaphore(0);

        this.mNfcToOperation = new WriteEmailNfc(taskCallback,
                opCallback,
                nfcWriteUtility
        );

        // TODO : Thread in thread. mAsyncWriteOperation launches a thread on its own, so we lose control.

        //this.mNfcToOperation;
        mSemaphore.acquire();
    }

    protected boolean checkResult(String value) {
        return mTestUtilities.checkResult(value);
    }

    protected TaskCallback getSucceedingAsyncUiCallback() {
        return new TaskCallback() {
            @Override
            public void onReturn(Boolean result) {
                assertTrue(result);
            }

            @Override
            public void onProgressUpdate(Boolean... values) {
                assertTrue(values[0]);
            }

            @Override
            public void onError(Exception e) {
                fail(e.getMessage());
            }
        };
    }

    protected TaskCallback getFailingAsyncUiCallback(){
        return new TaskCallback() {
            @Override
            public void onReturn(Boolean result) {
                assertFalse(result);
            }

            @Override
            public void onProgressUpdate(Boolean... values) {
                assertTrue(values[0]);
            }

            @Override
            public void onError(Exception e) {
                fail(e.getMessage());
            }
        };
    }
    protected void release() {
        mSemaphore.release();
    }
}
