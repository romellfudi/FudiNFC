package com.romellfudi.fudinfc.gear.interfaces;

import android.nfc.FormatException;

import com.romellfudi.fudinfc.util.exceptions.InsufficientCapacityException;
import com.romellfudi.fudinfc.util.exceptions.ReadOnlyTagException;
import com.romellfudi.fudinfc.util.exceptions.TagNotPresentException;
import com.romellfudi.fudinfc.util.interfaces.NfcWriteUtility;

public interface OpCallback {

    /**
     * Executed on background thread, do NOT perform any UI logic here !
     * @return
     */

    boolean performWrite(NfcWriteUtility writeUtility) throws ReadOnlyTagException, InsufficientCapacityException, TagNotPresentException, FormatException;
}
