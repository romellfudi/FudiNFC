package com.romellfudi.fudinfc.util.interfaces;

import android.nfc.FormatException;
import android.nfc.NdefMessage;

import org.jetbrains.annotations.NotNull;

import com.romellfudi.fudinfc.util.exceptions.InsufficientCapacityException;
import com.romellfudi.fudinfc.util.exceptions.ReadOnlyTagException;


public interface NfcMessageUtility {
    /**
     * @param urlAddress
     *         The url, auto-prefixed with the http://www. header
     * @return true if successful
     */
    NdefMessage createUri(@NotNull String urlAddress) throws FormatException;

    /**
     * Creates a telephone number - NdefMessage
     *
     * @param telephone
     *         number to create
     * @return true if success
     */
    NdefMessage createTel(@NotNull String telephone) throws FormatException;

    /**
     * Create SMS - NdefMessage. Due to a bug in Android this is not correctly implemented by the OS.
     *
     * @param message
     *         to send to the person
     * @param number
     *         of the recipient
     * @return true if success
     */
    NdefMessage createSms(@NotNull String number, String message) throws FormatException;

    /**
     * Creates a Geolocation - NdefMessage.
     * @param latitude
     *         maximum 6 decimals
     * @param longitude
     *         maximum 6 DECIMALS
     * @return true if success
     */
    NdefMessage createGeolocation(Double latitude, Double longitude) throws FormatException;

    /**
     * Create recipient, subject and message email - NdefMessage
     *
     * @param recipient
     *         to whom the mail should be sent
     * @param subject
     *         of the email
     * @param message
     *         body of the email
     * @return true if success
     */
    NdefMessage createEmail(@NotNull String recipient, String subject, String message) throws FormatException;

    /**
     * Create the bluetooth address - NdefMessage
     *
     * @param macAddress
     *         to create NdefMessage. Must be in format XX:XX:XX:XX:XX:XX, separator may differ
     * @return true if success
     */
    NdefMessage createBluetoothAddress(@NotNull String macAddress) throws InsufficientCapacityException, FormatException, ReadOnlyTagException;


    /**
     * Create the message with plain text
     * @param text to transfer
     * @return prepared NdefMessage
     * @throws FormatException
     */
    NdefMessage createText(@NotNull String text) throws FormatException;

    /**
     * Create the address with the given header
     * @see #createUri(String)
     */
    NdefMessage createUri(String urlAddress, byte payloadHeader) throws FormatException;
}
