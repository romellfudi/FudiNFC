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

public class WriteBluetoothNfc extends Nfc {

    /**
     * Instantiates a new WriteBluetoothNfc.
     *
     * @param taskCallback the async ui callback
     */
    public WriteBluetoothNfc(TaskCallback taskCallback) {
        super(taskCallback);
    }

    /**
     * Instantiates a new WriteBluetoothNfc.
     *
     * @param taskCallback the async ui callback
     * @param opCallback the async operation callback
     */
    public WriteBluetoothNfc(@Nullable TaskCallback taskCallback, @NotNull OpCallback opCallback) {
        super(taskCallback, opCallback);
    }

    /**
     * Instantiates a new WriteBluetoothNfc.
     *
     * @param taskCallback the async ui callback
     * @param opCallback the async operation callback
     * @param nfcWriteUtility the nfc write utility
     */
    public WriteBluetoothNfc(@Nullable TaskCallback taskCallback, @NotNull OpCallback opCallback, @NotNull NfcWriteUtility nfcWriteUtility) {
        super(taskCallback, opCallback, nfcWriteUtility);
    }

    /**
     * @see Nfc#executeWriteOperation(android.content.Intent, Object...)
     */
    @Override
    public void executeWriteOperation(final Intent intent, final Object... args) {
        if (checkStringArguments(args.getClass()) || args.length != 2 || intent == null) {
            throw new UnsupportedOperationException("Invalid arguments");
        }

        setAsyncOperationCallback(new OpCallback() {
            @Override
            public boolean performWrite(NfcWriteUtility writeUtility) throws ReadOnlyTagException, InsufficientCapacityException, TagNotPresentException, FormatException {
                return writeUtility.writeSmsToTagFromIntent((String) args[0], (String) args[1], intent);
            }
        });
        executeWriteOperation();
    }
}
