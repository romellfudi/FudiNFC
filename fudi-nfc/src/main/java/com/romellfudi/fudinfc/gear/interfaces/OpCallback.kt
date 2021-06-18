/*
 * Copyright (c) 2020. BoostTag E.I.R.L. Romell D.Z.
 * All rights reserved
 * porfile.romellfudi.com
 */
package com.romellfudi.fudinfc.gear.interfaces

import android.nfc.FormatException
import com.romellfudi.fudinfc.util.exceptions.InsufficientCapacityException
import com.romellfudi.fudinfc.util.exceptions.ReadOnlyTagException
import com.romellfudi.fudinfc.util.exceptions.TagNotPresentException
import com.romellfudi.fudinfc.util.interfaces.NfcWriteUtility

interface OpCallback {
    /**
     * Executed on background thread, do NOT perform any UI logic here !
     * @return
     */
    @Throws(
        ReadOnlyTagException::class,
        InsufficientCapacityException::class,
        TagNotPresentException::class,
        FormatException::class
    )
    fun performWrite(writeUtility: NfcWriteUtility?): Boolean
}