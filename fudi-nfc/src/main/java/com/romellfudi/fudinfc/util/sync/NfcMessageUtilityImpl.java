package com.romellfudi.fudinfc.util.sync;

import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.telephony.PhoneNumberUtils;

import org.jetbrains.annotations.NotNull;

import java.nio.charset.Charset;

import com.romellfudi.fudinfc.util.constants.NfcHead;
import com.romellfudi.fudinfc.util.constants.NfcType;
import com.romellfudi.fudinfc.util.interfaces.NfcMessageUtility;

public class NfcMessageUtilityImpl implements NfcMessageUtility {

    private static final String TAG = NfcMessageUtilityImpl.class.getCanonicalName();


    // Source : http://stackoverflow.com/questions/12295711/split-a-string-at-every-nth-position
    private static String[] splitStringEvery(String s, int interval) {
        int arrayLength = (int) Math.ceil(((s.length() / (double) interval)));
        String[] result = new String[arrayLength];

        int j = 0;
        int lastIndex = result.length - 1;
        for (int i = 0; i < lastIndex; i++, j += interval) {
            result[i] = s.substring(j, j + interval);
        } //Add the last bit
        result[lastIndex] = s.substring(j);

        return result;
    }

    /**
     * Internal method to write TNF to tag
     *
     * @param tnfType
     *         e.g. NdefRecord.TNF_WELL_KNOWN
     * @param type
     *         e.g. NdefRecord.RTD_URI
     * @param payload
     *         byte form of the content
     *
     * @return true if success
     */
    private NdefMessage createNdefMessage(short tnfType, byte[] type, byte[] payload) {
        NdefRecord record = new NdefRecord(tnfType,
                type, new byte[0],
                payload);
        return new NdefMessage(record);
    }

    /**
     * {@inheritDoc}
     * Precondition :  UrlAddress should not be null
     */
    @Override
    public NdefMessage createUri(@NotNull String urlAddress) throws FormatException {
        return createUriMessage(urlAddress, NfcHead.HTTP_WWW);
    }

    /**
     * {@inheritDoc}
     * Precondition :  Telephone should not be null
     */
    @Override
    public NdefMessage createTel(@NotNull String telephone) throws FormatException {
        telephone = telephone.startsWith("+") ? "+" + telephone.replaceAll("\\D", "") : telephone.replaceAll("\\D", "");
        if (!PhoneNumberUtils.isGlobalPhoneNumber(telephone)) {
            throw new FormatException();
        }

        return createUriMessage(telephone, NfcHead.TEL);
    }

    /**
     * {@inheritDoc}
     * Precondition :  At least number should not be null
     */
    @Override
    public NdefMessage createSms(@NotNull String number, String message) throws FormatException {
        number = number.startsWith("+") ? "+" + number.replaceAll("\\D", "") : number.replaceAll("\\D", "");
        if (!PhoneNumberUtils.isGlobalPhoneNumber((number))) {
            throw new FormatException();
        }
        String smsPattern = "sms:" + number + "?body=" + message;
        //String externalType = "nfclab.com:smsService";
        return createUriMessage(smsPattern, NfcHead.CUSTOM_SCHEME);
    }

    /**
     * {@inheritDoc}
     * Precondition : lat- and longitude, max 6 decimals
     */
    @Override
    public NdefMessage createGeolocation(Double latitude, Double longitude) throws FormatException {
        latitude = Math.round(latitude * Math.pow(10, 6)) / Math.pow(10, 6);
        longitude = Math.round(longitude * Math.pow(10, 6)) / Math.pow(10, 6);
        String address = "geo:" + latitude.floatValue() + "," + longitude.floatValue();
        String externalType = "nfclab.com:geoService";

        return createUriMessage(address, NfcHead.CUSTOM_SCHEME);
    }


    /**
     * {@inheritDoc}
     * Precondition : At least recipient should not be null
     */
    @Override
    public NdefMessage createEmail(@NotNull String recipient, String subject, String message) throws FormatException {
        subject = (subject != null) ? subject : "";
        message = (message != null) ? message : "";
        String address = recipient + "?subject=" + subject + "&body=" + message;

        return createUriMessage(address, NfcHead.MAILTO);
    }

    /**
     * {@inheritDoc}
     * Precondition : macAddress should not be null
     */
    @Override
    public NdefMessage createBluetoothAddress(@NotNull String macAddress) throws FormatException {
        byte[] payload = convertBluetoothToNdefFormat(macAddress);
        NdefRecord record = new NdefRecord(NdefRecord.TNF_MIME_MEDIA, NfcType.BLUETOOTH_AAR, null, payload);

        return new NdefMessage(record);
    }

    /**
     * {@inheritDoc}
     * Precondition : Text should not be null, encoded in UTF8
     */
    @Override
    public NdefMessage createText(@NotNull String text) {
        byte[] payload = new byte[text.getBytes().length+1];
        System.arraycopy(text.getBytes(),0,payload,1,text.length());

        return createNdefMessage(NdefRecord.TNF_WELL_KNOWN,
                NdefRecord.RTD_TEXT,payload);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NdefMessage createUri(String urlAddress, byte payloadHeader) throws FormatException {
        return createUriMessage(urlAddress,payloadHeader);
    }

    /**
     * Write URI to tag
     *
     * @param urlAddress
     *         to write to tag
     * @param payloadHeader
     *         defining the prefix
     *
     * @return true if success
     */
    private NdefMessage createUriMessage(@NotNull String urlAddress, byte payloadHeader) {
        byte[] uriField = urlAddress.getBytes(Charset.forName("US-ASCII"));
        byte[] payload = new byte[uriField.length + 1];
        payload[0] = payloadHeader; // Marks the prefix
        System.arraycopy(uriField, 0, payload, 1, uriField.length);
        return createNdefMessage(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_URI, payload);
    }

    /**
     * The MAC-Address has to be in reverse order, the first 2 bits indicate the length.
     *
     * @param bluetoothAddress
     *         to be transformed.
     *
     * @return MAC-Address in the right format
     *
     * http://members.nfc-forum.org/resources/AppDocs/NFCForum_AD_BTSSP_1_0.pdf
     */
    private byte[] convertBluetoothToNdefFormat(String bluetoothAddress) {
        byte[] res = new byte[8];
        String[] parts = bluetoothAddress.split(".(?=[\\w\\d]{2})");

        if (bluetoothAddress.length() == 12) {
            parts = splitStringEvery(bluetoothAddress, 2);
        }

        if (parts.length != 6) {
            return res;
        }

        // Leave 1st 2 bits untouched, have to be adjusted in order to make this recognisable
        for (int i = 5; i >= 0; i--) {
            res[7 - i] = (byte) Integer.parseInt(parts[i], 16);
            System.out.println(res[5 - i]);
        }

        res[0] = (byte) (res.length % 256);
        res[1] = (byte) (res.length / 256);
        return res;
    }

}