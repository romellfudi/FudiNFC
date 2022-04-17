/*
 * Copyright (c) 2020. BoostTag E.I.R.L. Romell D.Z.
 * All rights reserved
 * porfile.romellfudi.com
 */
package com.romellfudi.fudinfc.gear

import android.os.AsyncTask
import android.util.Log
import com.romellfudi.fudinfc.gear.GenericTask
import com.romellfudi.fudinfc.gear.interfaces.OpCallback
import com.romellfudi.fudinfc.gear.interfaces.TaskCallback
import com.romellfudi.fudinfc.util.interfaces.NfcWriteUtility
import com.romellfudi.fudinfc.util.sync.NfcWriteUtilityImpl

class GenericTask : AsyncTask<Void?, Boolean?, Boolean> {
    private val mNfcWriteUtility: NfcWriteUtility
    private var mTaskCallback: TaskCallback?
    private var mOpCallback: OpCallback?
    private var error: Exception? = null

    /**
     * @param taskCallback
     * callback executed on UI thread
     * @param opCallback
     * callback to be executed in the background
     *
     * @throws java.lang.NullPointerException
     * if (opCallback == null)
     */
    constructor(taskCallback: TaskCallback?, opCallback: OpCallback) {
        mTaskCallback = taskCallback
        mOpCallback = opCallback
        mNfcWriteUtility = NfcWriteUtilityImpl()
    }

    /**
     * @param nfcWriteUtility
     * to be passed to the opCallback
     *
     * @throws java.lang.NullPointerException
     * if (nfcWriteUtility == null)
     * @see .GenericTask
     */
    constructor(
        taskCallback: TaskCallback?,
        opCallback: OpCallback,
        nfcWriteUtility: NfcWriteUtility
    ) {
        mTaskCallback = taskCallback
        mOpCallback = opCallback
        mNfcWriteUtility = nfcWriteUtility
    }

    /**
     * Job to be done in the background.
     * Any error is rerouted to [.onPostExecute], and hence to the callback onError()
     * @param params
     * @return OpCallback.performWrite() == true
     */
    override fun doInBackground(vararg params: Void?): Boolean {
        var res = false
        try {
            Log.d(TAG, "Writing ..")
            publishProgress(true)
            mOpCallback?.let {
                res = it.performWrite(mNfcWriteUtility)
            } ?: run {
                error = NullPointerException("OperationCallback is null")
            }
        } catch (e: Exception) {
            Log.w(TAG, e)
            mTaskCallback?.onError(e)
            error = e
        }

        // Remove tag from intent in order to prevent writing to a not present tag
        return res
    }

    /**
     * Fire the onReturn.
     * If the error != null then onError is executed as well.
     * @param result
     */
    override fun onPostExecute(result: Boolean) {
        super.onPostExecute(result)
        mOpCallback?.run {
            error?.let { mTaskCallback?.onError(it) }
            mTaskCallback?.onReturn(result)
        }
    }

    override fun onProgressUpdate(vararg values: Boolean?) {
        super.onProgressUpdate(*values)
        mTaskCallback?.onProgressUpdate(*values)
    }

    companion object {
        private val TAG = GenericTask::class.java.simpleName
    }
}