package com.romellfudi.fudinfc.util.interfaces;

import android.content.Intent;

public interface NfcToOperation {
    /**
     * Method executed asynchronously, do **NOT** execute any UI logic in here! Funky stuff may occur
     */
    void executeWriteOperation();

    /**
     * Method executed asynchronously, do **NOT** execute any UI logic in here ! Funky stuff may occur
     * @param intent to be passed to the write utility
     * @param args to be passed to the method
     */
    void executeWriteOperation(Intent intent, Object... args);
}
