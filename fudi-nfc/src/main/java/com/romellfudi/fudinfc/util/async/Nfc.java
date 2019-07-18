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

public abstract class Nfc implements NfcToOperation {

    protected NfcWriteUtility mNfcWriteUtility;

    protected OpCallback mOpCallback;
    protected TaskCallback mTaskCallback;

    public Nfc(final TaskCallback taskCallback) {
        setAsyncUiCallback(taskCallback);
    }

    public Nfc(final TaskCallback taskCallback, NfcWriteUtility nfcWriteUtility) {
        setAsyncUiCallback(taskCallback);
        setNfcWriteUtility(nfcWriteUtility);
    }

    public Nfc(final @Nullable TaskCallback taskCallback, final @NotNull OpCallback opCallback) {
        this(taskCallback);
        setAsyncOperationCallback(opCallback);
    }

    public Nfc(final @Nullable TaskCallback taskCallback, final @NotNull OpCallback opCallback,
            final @NotNull NfcWriteUtility nfcWriteUtility) {
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

    public void executeWriteOperation() {
        if (getNfcWriteUtility() != null) {
            new GenericTask(mTaskCallback, getAsyncOperationCallback(), getNfcWriteUtility())
                    .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            new GenericTask(mTaskCallback, getAsyncOperationCallback())
                    .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    public abstract void executeWriteOperation(Intent intent, Object... args);

    protected boolean checkStringArguments(Class<?> type) {
        return (type.equals(String[].class));
    }

    protected boolean checkDoubleArguments(Class<?> type) {
        return type.equals(Double[].class);
    }
}
