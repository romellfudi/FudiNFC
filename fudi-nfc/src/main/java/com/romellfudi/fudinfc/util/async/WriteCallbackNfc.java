package com.romellfudi.fudinfc.util.async;

import android.content.Intent;

import com.romellfudi.fudinfc.gear.interfaces.OpCallback;
import com.romellfudi.fudinfc.gear.interfaces.TaskCallback;
import com.romellfudi.fudinfc.util.interfaces.NfcWriteUtility;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WriteCallbackNfc extends Nfc {

    /**
     * Instantiates a new WriteCallbackNfc.
     *
     * @param taskCallback the async ui callback
     */
    public WriteCallbackNfc(TaskCallback taskCallback) {
        super(taskCallback);
    }

    /**
     * Instantiates a new WriteCallbackNfc.
     *
     * @param taskCallback           the async ui callback
     * @param opCallback the async operation callback
     */
    public WriteCallbackNfc(@Nullable TaskCallback taskCallback, @NotNull OpCallback opCallback) {
        super(taskCallback, opCallback);
    }

    /**
     * Instantiates a new WriteCallbackNfc.
     *
     * @param taskCallback           the async ui callback
     * @param opCallback the async operation callback
     * @param nfcWriteUtility        the nfc write utility
     */
    public WriteCallbackNfc(@Nullable TaskCallback taskCallback, @NotNull OpCallback opCallback, @NotNull NfcWriteUtility nfcWriteUtility) {
        super(taskCallback, opCallback, nfcWriteUtility);
    }

    @Override
    public void executeWriteOperation(Intent intent, Object... args) {
        super.executeWriteOperation();
    }
}
