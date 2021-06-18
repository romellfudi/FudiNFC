/*
 * Copyright (c) 2020. BoostTag E.I.R.L. Romell D.Z.
 * All rights reserved
 * porfile.romellfudi.com
 */
package com.romellfudi.fudinfc.util.async

import android.content.Intent
import android.os.AsyncTask
import com.romellfudi.fudinfc.gear.GenericTask
import com.romellfudi.fudinfc.gear.interfaces.OpCallback
import com.romellfudi.fudinfc.gear.interfaces.TaskCallback
import com.romellfudi.fudinfc.util.interfaces.NfcToOperation
import com.romellfudi.fudinfc.util.interfaces.NfcWriteUtility

abstract class Nfc : NfcToOperation {
    protected var nfcWriteUtility: NfcWriteUtility? = null
    protected var asyncOperationCallback: OpCallback? = null
    protected var asyncUiCallback: TaskCallback? = null

    constructor(taskCallback: TaskCallback?) {
        asyncUiCallback = taskCallback
    }

    constructor(taskCallback: TaskCallback?, nfcWriteUtility: NfcWriteUtility?) {
        asyncUiCallback = taskCallback
        this.nfcWriteUtility = nfcWriteUtility
    }

    constructor(taskCallback: TaskCallback?, opCallback: OpCallback) : this(taskCallback) {
        asyncOperationCallback = opCallback
    }

    constructor(
        taskCallback: TaskCallback?, opCallback: OpCallback,
        nfcWriteUtility: NfcWriteUtility
    ) : this(taskCallback, nfcWriteUtility) {
        asyncOperationCallback = opCallback
    }

    override fun executeWriteOperation() {
        if (nfcWriteUtility != null) {
            GenericTask(asyncUiCallback, asyncOperationCallback!!, nfcWriteUtility!!)
                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
        } else {
            GenericTask(asyncUiCallback, asyncOperationCallback!!)
                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
        }
    }

    abstract override fun executeWriteOperation(intent: Intent?, vararg args: Any?)
    protected fun checkStringArguments(type: Class<*>): Boolean {
        return type == Array<String>::class.java
    }

    protected fun checkDoubleArguments(type: Class<*>): Boolean {
        return type == Array<Double>::class.java
    }
}