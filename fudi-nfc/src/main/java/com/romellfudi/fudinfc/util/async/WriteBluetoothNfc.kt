/*
 * Copyright (c) 2020. BoostTag E.I.R.L. Romell D.Z.
 * All rights reserved
 * porfile.romellfudi.com
 */
package com.romellfudi.fudinfc.util.async

import android.content.Intent
import android.nfc.FormatException
import com.romellfudi.fudinfc.gear.interfaces.OpCallback
import com.romellfudi.fudinfc.gear.interfaces.TaskCallback
import com.romellfudi.fudinfc.util.exceptions.InsufficientCapacityException
import com.romellfudi.fudinfc.util.exceptions.ReadOnlyTagException
import com.romellfudi.fudinfc.util.exceptions.TagNotPresentException
import com.romellfudi.fudinfc.util.interfaces.NfcWriteUtility

class WriteBluetoothNfc : Nfc {
    /**
     * Instantiates a new WriteBluetoothNfc.
     *
     * @param taskCallback the async ui callback
     */
    constructor(taskCallback: TaskCallback?) : super(taskCallback) {}

    /**
     * Instantiates a new WriteBluetoothNfc.
     *
     * @param taskCallback the async ui callback
     * @param opCallback the async operation callback
     */
    constructor(taskCallback: TaskCallback?, opCallback: OpCallback) : super(
        taskCallback,
        opCallback
    ) {
    }

    /**
     * Instantiates a new WriteBluetoothNfc.
     *
     * @param taskCallback the async ui callback
     * @param opCallback the async operation callback
     * @param nfcWriteUtility the nfc write utility
     */
    constructor(
        taskCallback: TaskCallback?,
        opCallback: OpCallback,
        nfcWriteUtility: NfcWriteUtility
    ) : super(taskCallback, opCallback, nfcWriteUtility) {
    }

    /**
     * @see Nfc.executeWriteOperation
     */
    override fun executeWriteOperation(intent: Intent?, vararg args: Any?) {
        if (checkStringArguments(args.javaClass) || args.size != 2 || intent == null) {
            throw UnsupportedOperationException("Invalid arguments")
        }
        asyncOperationCallback = object : OpCallback {
            @Throws(
                ReadOnlyTagException::class,
                InsufficientCapacityException::class,
                TagNotPresentException::class,
                FormatException::class
            )
            override fun performWrite(writeUtility: NfcWriteUtility?) = (writeUtility?.apply {
                writeSmsToTagFromIntent(args[0] as String, args[1] as String, intent)
            } ?: false) as Boolean
        }
        executeWriteOperation()
    }
}