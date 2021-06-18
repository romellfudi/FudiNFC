/*
 * Copyright (c) 2020. BoostTag E.I.R.L. Romell D.Z.
 * All rights reserved
 * porfile.romellfudi.com
 */

package com.romellfudi.fudinfc.gear;

import android.os.AsyncTask;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import com.romellfudi.fudinfc.gear.interfaces.OpCallback;
import com.romellfudi.fudinfc.gear.interfaces.TaskCallback;
import com.romellfudi.fudinfc.util.interfaces.NfcWriteUtility;
import com.romellfudi.fudinfc.util.sync.NfcWriteUtilityImpl;

public class GenericTask extends AsyncTask<Void, Boolean, Boolean> {

    private static final String TAG = GenericTask.class.getSimpleName();
    private final NfcWriteUtility mNfcWriteUtility;

    private TaskCallback mTaskCallback;
    private OpCallback mOpCallback;
    private Exception error;

    /**
     * @param taskCallback
     *         callback executed on UI thread
     * @param opCallback
     *         callback to be executed in the background
     *
     * @throws java.lang.NullPointerException
     *         if (opCallback == null)
     */
    public GenericTask(TaskCallback taskCallback, @NotNull OpCallback opCallback) {
        mTaskCallback = taskCallback;
        mOpCallback = opCallback;
        mNfcWriteUtility = new NfcWriteUtilityImpl();
    }

    /**
     * @param nfcWriteUtility
     *         to be passed to the opCallback
     *
     * @throws java.lang.NullPointerException
     *         if (nfcWriteUtility == null)
     * @see #GenericTask(TaskCallback, OpCallback)
     */
    public GenericTask(TaskCallback taskCallback, @NotNull OpCallback opCallback, @NotNull NfcWriteUtility nfcWriteUtility) {
        mTaskCallback = taskCallback;
        mOpCallback = opCallback;
        mNfcWriteUtility = nfcWriteUtility;
    }

    /**
     * Job to be done in the background.
     * Any error is rerouted to {@link #onPostExecute(Boolean)}, and hence to the callback onError()
     * @param params
     * @return OpCallback.performWrite() == true
     */
    @Override
    protected Boolean doInBackground(Void... params) {
        Log.d(TAG, "Writing ..");

        boolean res = false;
        try {
            publishProgress(true);
            if (mOpCallback != null) {
                res = mOpCallback.performWrite(mNfcWriteUtility);
            } else {
                error = new NullPointerException("OperationCallback is null");
            }

        } catch (Exception e) {
            Log.w(TAG, e);
            mTaskCallback.onError(e);
            error = e;
        }

        // Remove tag from intent in order to prevent writing to a not present tag
        return res;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    /**
     * Fire the onReturn.
     * If the error != null then onError is executed as well.
     * @param result
     */
    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        if (mOpCallback != null && mTaskCallback != null) {
            if (error != null) {
                mTaskCallback.onError(error);
            }
            mTaskCallback.onReturn(result);
        }
    }

    @Override
    protected void onProgressUpdate(Boolean... values) {
        super.onProgressUpdate(values);
        if (mTaskCallback != null) {
            mTaskCallback.onProgressUpdate(values);
        }
    }

}