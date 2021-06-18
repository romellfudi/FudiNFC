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

public class WriteUriNfc extends Nfc {

    /**
     * Instantiates a new WriteUriNfc.
     *
     * @param taskCallback the async ui callback
     */
    public WriteUriNfc(TaskCallback taskCallback) {
        super(taskCallback);
    }

    /**
     * Instantiates a new WriteUriNfc.
     *
     * @param taskCallback the async ui callback
     * @param opCallback the async operation callback
     */
    public WriteUriNfc(@Nullable TaskCallback taskCallback, @NotNull OpCallback opCallback) {
        super(taskCallback, opCallback);
    }

    /**
     * Instantiates a new WriteUriNfc.
     *
     * @param taskCallback the async ui callback
     * @param opCallback the async operation callback
     * @param nfcWriteUtility the nfc write utility
     */
    public WriteUriNfc(@Nullable TaskCallback taskCallback, @NotNull OpCallback opCallback, @NotNull NfcWriteUtility nfcWriteUtility) {
        super(taskCallback, opCallback, nfcWriteUtility);
    }

    @Override
    public void executeWriteOperation(final Intent intent, final Object... args) {
        if (!checkStringArguments(args.getClass()) || args.length != 1 || !args[0].equals("")){
            throw new UnsupportedOperationException("Invalid arguments!");
        }

        setAsyncOperationCallback(new OpCallback() {
            @Override
            public boolean performWrite(NfcWriteUtility writeUtility) throws ReadOnlyTagException, InsufficientCapacityException, TagNotPresentException, FormatException {
                return writeUtility.writeUriToTagFromIntent((String) args[0],intent);
            }
        });

        super.executeWriteOperation();
    }
}
