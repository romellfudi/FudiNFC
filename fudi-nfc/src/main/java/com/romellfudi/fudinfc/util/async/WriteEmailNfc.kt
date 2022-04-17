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

/**
 * @author Daneo Van Overloop
 * NfcLibrary
 * Created on 22/04/14.
 */
class WriteEmailNfc : Nfc {
    /**
     * Instantiates a new WriteEmailNfc.
     *
     * @param uiCallback the ui callback
     * @see Nfc.Nfc
     */
    constructor(uiCallback: TaskCallback?) : super(uiCallback) {}

    /**
     * Instantiates a new WriteEmailNfc.
     *
     * @param taskCallback the async ui callback
     * @param opCallback the async operation callback
     * @see Nfc.Nfc
     */
    constructor(taskCallback: TaskCallback?, opCallback: OpCallback) : super(
        taskCallback,
        opCallback
    ) {
    }

    /**
     * Instantiates a new WriteEmailNfc.
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

    override fun executeWriteOperation(intent: Intent?, vararg args: Any?) {
        if (!checkStringArguments(args.javaClass) || args.isEmpty() || intent == null) {
            throw UnsupportedOperationException("Incorrect arguments")
        }
        val recipient = args[0] as String
        val subject = if (args.size > 1) args[1] as String else null
        val message = if (args.size == 3) args[2] as String else null
        asyncOperationCallback = object : OpCallback {
            @Throws(
                ReadOnlyTagException::class,
                InsufficientCapacityException::class,
                TagNotPresentException::class,
                FormatException::class
            )
            override fun performWrite(writeUtility: NfcWriteUtility?) = (writeUtility?.apply {
                writeEmailToTagFromIntent(recipient, subject, message, intent)
            } ?: false) as Boolean
        }
        super.executeWriteOperation()
    }
}