/*
 * WriteEmailNfc.java
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
