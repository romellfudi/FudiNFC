/*
 * Nfc.java
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

import android.content.Intent;
import android.os.AsyncTask;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.romellfudi.fudinfc.gear.GenericTask;
import com.romellfudi.fudinfc.gear.interfaces.OpCallback;
import com.romellfudi.fudinfc.gear.interfaces.TaskCallback;
import com.romellfudi.fudinfc.util.interfaces.NfcToOperation;
import com.romellfudi.fudinfc.util.interfaces.NfcWriteUtility;

/**
 * Class utilizing a {@link com.romellfudi.fudinfc.gear.GenericTask} to write async
 * @author Daneo Van Overloop
 * NfcLibrary
 */
public abstract class Nfc implements NfcToOperation {

    protected NfcWriteUtility mNfcWriteUtility;

    protected OpCallback mOpCallback;
    protected TaskCallback mTaskCallback;

    /**
     * @param taskCallback
     *         callback executed on the UI thread
     */
    public Nfc(final TaskCallback taskCallback) {
        setAsyncUiCallback(taskCallback);
    }

    /**
     * @param nfcWriteUtility
     *         implementation of {@link com.romellfudi.fudinfc.util.interfaces.NfcWriteUtility} to use
     *
     * @see #Nfc(TaskCallback)
     */
    public Nfc(final TaskCallback taskCallback, NfcWriteUtility nfcWriteUtility) {
        setAsyncUiCallback(taskCallback);
        setNfcWriteUtility(nfcWriteUtility);
    }

    /**
     * @param opCallback
     *         callback executed on a background thread
     *
     * @see #Nfc(TaskCallback)
     */
    public Nfc(final @Nullable TaskCallback taskCallback, final @NotNull OpCallback opCallback) {
        this(taskCallback);
        setAsyncOperationCallback(opCallback);
    }

    /**
     * Constructor taking an AsyncUI-, OpCallback and an {@link com.romellfudi.fudinfc.util.interfaces.NfcWriteUtility}
     *
     * @param nfcWriteUtility
     *         implementation of {@link com.romellfudi.fudinfc.util.interfaces.NfcWriteUtility} to use
     *
     * @see #Nfc(TaskCallback, OpCallback)
     */
    public Nfc(final @Nullable TaskCallback taskCallback, final @NotNull OpCallback opCallback, final @NotNull NfcWriteUtility nfcWriteUtility) {
        this(taskCallback, nfcWriteUtility);
        setAsyncOperationCallback(opCallback);
    }

    protected TaskCallback getAsyncUiCallback() {
        return mTaskCallback;
    }

    protected void setAsyncUiCallback(TaskCallback taskCallback) {
        mTaskCallback = taskCallback;
    }

    protected OpCallback getAsyncOperationCallback() {
        return mOpCallback;
    }

    protected void setAsyncOperationCallback(OpCallback opCallback) {
        mOpCallback = opCallback;
    }

    protected NfcWriteUtility getNfcWriteUtility() {
        return mNfcWriteUtility;
    }

    protected void setNfcWriteUtility(NfcWriteUtility nfcWriteUtility) {
        mNfcWriteUtility = nfcWriteUtility;
    }

    /**
     * Creates an async task with the current {@link #getAsyncOperationCallback()} as action
     *
     * @throws java.lang.NullPointerException
     *         if {@link #getAsyncOperationCallback()} is null
     * @see NfcToOperation#executeWriteOperation()
     */
    @Override
    public void executeWriteOperation() {
        if (getNfcWriteUtility() != null) {
            new GenericTask(mTaskCallback, getAsyncOperationCallback(), getNfcWriteUtility()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            new GenericTask(mTaskCallback, getAsyncOperationCallback()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    /**
     * Convenience method making it possible not to define an {@link OpCallback} at construction time but rather pass arguments and let the implementation handle it
     * Should set the OpCallback, and then call {@link #executeWriteOperation()}
     *
     * @param intent
     *         to be passed to the write utility
     * @param args
     *         to be passed to the method
     *
     * @see #setAsyncOperationCallback(OpCallback)
     * @see NfcToOperation#executeWriteOperation()
     */
    @Override
    public abstract void executeWriteOperation(Intent intent, Object... args);

    /**
     * Used to check whether the passed type is equal to a String array
     *
     * @param type
     *         to compare with
     *
     * @return type.equals(String[].class)
     */
    protected boolean checkStringArguments(Class<?> type) {
        return (type.equals(String[].class));
    }

    /**
     * Used to check whether the passed type is equal to a Double array
     *
     * @param type
     *         to compare with
     *
     * @return type.equals(Double[].class)
     */
    protected boolean checkDoubleArguments(Class<?> type) {
        return type.equals(Double[].class);
    }
}
