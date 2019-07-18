
package com.romellfudi.fudinfc.util.interfaces;

import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.Tag;

import com.romellfudi.fudinfc.util.exceptions.InsufficientCapacityException;
import com.romellfudi.fudinfc.util.exceptions.ReadOnlyTagException;

public interface WriteUtility extends NdefWrite {


    /**
     * Writes towards a {@link android.nfc.Tag}
     *
     * @param message
     *         to write
     * @param tag
     *         to write to
     *
     * @return true if success
     *
     * @see #writeToNdef(android.nfc.NdefMessage, android.nfc.tech.Ndef)
     */
    boolean writeSafelyToTag(NdefMessage message, Tag tag);

    /**
     * Write the given message to the tag
     *
     * @param message
     *         to write
     * @param tag
     *         to write to
     *
     * @return true if success
     *
     * @throws android.nfc.FormatException if the message is in an incorrect format
     * @throws com.romellfudi.fudinfc.util.exceptions.InsufficientCapacityException if there is not enough space available on the tag
     * @throws com.romellfudi.fudinfc.util.exceptions.ReadOnlyTagException when attempting to write to a read-only tag
     */
    boolean writeToTag(NdefMessage message, Tag tag) throws FormatException, ReadOnlyTagException, InsufficientCapacityException;

    /**
     * Used to mark the following operation as readonly
     * @return an instance of WriteUtility in order to chain
     */
    WriteUtility makeOperationReadOnly();
}
