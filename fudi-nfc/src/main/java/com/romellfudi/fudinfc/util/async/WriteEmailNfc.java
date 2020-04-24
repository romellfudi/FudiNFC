/*
 * Copyright (c) 2020. BoostTag E.I.R.L. Romell D.Z.
 * All rights reserved
 * porfile.romellfudi.com
 */

package com.romellfudi.fudinfc.util.async;

import android.content.Intent;
import android.nfc.FormatException;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.romellfudi.fudinfc.util.exceptions.InsufficientCapacityException;
import com.romellfudi.fudinfc.util.exceptions.ReadOnlyTagException;
import com.romellfudi.fudinfc.util.exceptions.TagNotPresentException;
import com.romellfudi.fudinfc.gear.interfaces.OpCallback;
import com.romellfudi.fudinfc.gear.interfaces.TaskCallback;
import com.romellfudi.fudinfc.util.interfaces.NfcWriteUtility;

/**
 * @author Daneo Van Overloop
 * NfcLibrary
 * Created on 22/04/14.
 */
public class WriteEmailNfc extends Nfc {

    /**
     * Instantiates a new WriteEmailNfc.
     *
     * @param uiCallback the ui callback
     * @see Nfc#Nfc(TaskCallback)
     */
    public WriteEmailNfc(@Nullable TaskCallback uiCallback) {
        super(uiCallback);
    }

    /**
     * Instantiates a new WriteEmailNfc.
     *
     * @param taskCallback the async ui callback
     * @param opCallback the async operation callback
     * @see Nfc#Nfc(TaskCallback, OpCallback)
     */
    public WriteEmailNfc(@Nullable TaskCallback taskCallback, @NotNull OpCallback opCallback) {
        super(taskCallback, opCallback);
    }

    /**
     * Instantiates a new WriteEmailNfc.
     *
     * @param taskCallback the async ui callback
     * @param opCallback the async operation callback
     * @param nfcWriteUtility the nfc write utility
      */
    public WriteEmailNfc(@Nullable TaskCallback taskCallback, @NotNull OpCallback opCallback, @NotNull NfcWriteUtility nfcWriteUtility) {
        super(taskCallback, opCallback, nfcWriteUtility);
    }

    @Override
    public void executeWriteOperation(final Intent intent, Object... args) {
        if (!checkStringArguments(args.getClass()) || args.length == 0) {
            throw new UnsupportedOperationException("Incorrect arguments");
        }

        final String recipient = (String) args[0], subject = args.length > 1 ? (String) args[1] : null, message = args.length == 3 ? (String) args[2] : null;

        setAsyncOperationCallback(new OpCallback() {
            @Override
            public boolean performWrite(NfcWriteUtility writeUtility) throws ReadOnlyTagException, InsufficientCapacityException, TagNotPresentException, FormatException {

                return writeUtility.writeEmailToTagFromIntent(recipient, subject, message, intent);
            }
        });

        super.executeWriteOperation();
    }

}
