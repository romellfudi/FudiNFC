/*
 * Copyright (c) 2020. BoostTag E.I.R.L. Romell D.Z.
 * All rights reserved
 * porfile.romellfudi.com
 */
package com.romellfudi.fudinfc.util.sync

import android.nfc.FormatException
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.telephony.PhoneNumberUtils
import com.romellfudi.fudinfc.util.constants.NfcHead
import com.romellfudi.fudinfc.util.constants.NfcType
import com.romellfudi.fudinfc.util.interfaces.NfcMessageUtility
import java.nio.charset.Charset
import kotlin.math.ceil
import kotlin.math.pow
import kotlin.math.roundToInt

class NfcMessageUtilityImpl : NfcMessageUtility {
    /**
     * Internal method to write TNF to tag
     *
     * @param tnfType
     * e.g. NdefRecord.TNF_WELL_KNOWN
     * @param type
     * e.g. NdefRecord.RTD_URI
     * @param payload
     * byte form of the content
     *
     * @return true if success
     */
    private fun createNdefMessage(
        tnfType: Short,
        type: ByteArray,
        payload: ByteArray
    ): NdefMessage {
        val record = NdefRecord(
            tnfType,
            type, ByteArray(0),
            payload
        )
        return NdefMessage(record)
    }

    /**
     * {@inheritDoc}
     * Precondition :  UrlAddress should not be null
     */
    @Throws(FormatException::class)
    override fun createUri(urlAddress: String): NdefMessage {
        return createUriMessage(urlAddress, NfcHead.HTTP_WWW)
    }

    /**
     * {@inheritDoc}
     * Precondition :  Telephone should not be null
     */
    @Throws(FormatException::class)
    override fun createTel(telephone: String): NdefMessage {
        var telephone = telephone
        telephone = if (telephone.startsWith("+")) "+" + telephone.replace(
            "\\D".toRegex(),
            ""
        ) else telephone.replace("\\D".toRegex(), "")
        if (!PhoneNumberUtils.isGlobalPhoneNumber(telephone)) {
            throw FormatException()
        }
        return createUriMessage(telephone, NfcHead.TEL)
    }

    /**
     * {@inheritDoc}
     * Precondition :  At least number should not be null
     */
    @Throws(FormatException::class)
    override fun createSms(number: String, message: String?): NdefMessage {
        var number = number
        number = if (number.startsWith("+")) "+" + number.replace(
            "\\D".toRegex(),
            ""
        ) else number.replace("\\D".toRegex(), "")
        if (!PhoneNumberUtils.isGlobalPhoneNumber(number)) {
            throw FormatException()
        }
        val smsPattern = "sms:$number?body=$message"
        //String externalType = "nfclab.com:smsService";
        return createUriMessage(smsPattern, NfcHead.CUSTOM_SCHEME)
    }

    /**
     * {@inheritDoc}
     * Precondition : lat- and longitude, max 6 decimals
     */
    @Throws(FormatException::class)
    override fun createGeolocation(latitude: Double?, longitude: Double?): NdefMessage {
        val address = "geo:${latitude?.let {  (it * 10.0.pow(6.0)).roundToInt() / 10.0.pow(6.0) }}," +
                "${longitude?.let { (it * 10.0.pow(6.0)).roundToInt() / 10.0.pow(6.0) }}"
        return createUriMessage(address, NfcHead.CUSTOM_SCHEME)
    }

    /**
     * {@inheritDoc}
     * Precondition : At least recipient should not be null
     */
    @Throws(FormatException::class)
    override fun createEmail(recipient: String, subject: String?, message: String?): NdefMessage? {
        return createUriMessage("$recipient?subject=${subject ?: ""}&body=${message ?: ""}", NfcHead.MAILTO)
    }

    /**
     * {@inheritDoc}
     * Precondition : macAddress should not be null
     */
    @Throws(FormatException::class)
    override fun createBluetoothAddress(macAddress: String): NdefMessage {
        val payload = convertBluetoothToNdefFormat(macAddress)
        val record = NdefRecord(NdefRecord.TNF_MIME_MEDIA, NfcType.BLUETOOTH_AAR, null, payload)
        return NdefMessage(record)
    }

    /**
     * {@inheritDoc}
     * Precondition : Text should not be null, encoded in UTF8
     */
    override fun createText(text: String): NdefMessage {
        val payload = ByteArray(text.toByteArray().size + 1)
        System.arraycopy(text.toByteArray(), 0, payload, 1, text.length)
        return createNdefMessage(
            NdefRecord.TNF_WELL_KNOWN,
            NdefRecord.RTD_TEXT, payload
        )
    }

    /**
     * {@inheritDoc}
     */
    @Throws(FormatException::class)
    override fun createUri(urlAddress: String?, payloadHeader: Byte): NdefMessage {
        return urlAddress?.let { createUriMessage(it, payloadHeader) } ?: throw FormatException()
    }

    /**
     * Write URI to tag
     *
     * @param urlAddress
     * to write to tag
     * @param payloadHeader
     * defining the prefix
     *
     * @return true if success
     */
    private fun createUriMessage(urlAddress: String, payloadHeader: Byte): NdefMessage {
        val uriField = urlAddress.toByteArray(Charset.forName("US-ASCII"))
        val payload = ByteArray(uriField.size + 1)
        payload[0] = payloadHeader // Marks the prefix
        System.arraycopy(uriField, 0, payload, 1, uriField.size)
        return createNdefMessage(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_URI, payload)
    }

    /**
     * The MAC-Address has to be in reverse order, the first 2 bits indicate the length.
     *
     * @param bluetoothAddress
     * to be transformed.
     *
     * @return MAC-Address in the right format
     *
     * http://members.nfc-forum.org/resources/AppDocs/NFCForum_AD_BTSSP_1_0.pdf
     */
    private fun convertBluetoothToNdefFormat(bluetoothAddress: String): ByteArray {
        val res = ByteArray(8)
        var parts: Array<String?> =
            bluetoothAddress.split(".(?=[\\w\\d]{2})".toRegex()).toTypedArray()
        if (bluetoothAddress.length == 12) {
            parts = splitStringEvery(bluetoothAddress, 2)
        }
        if (parts.size != 6) {
            return res
        }
        for (i in 5 downTo 0) {
            res[7 - i] = parts[i]?.toInt(16)?.toByte() ?: 0
            println(res[5 - i])
        }
        res[0] = (res.size % 256).toByte()
        res[1] = (res.size / 256).toByte()
        return res
    }

    companion object {
        private fun splitStringEvery(s: String, interval: Int): Array<String?> {
            val arrayLength = ceil(s.length / interval.toDouble()).toInt()
            val result = arrayOfNulls<String>(arrayLength)
            var j = 0
            val lastIndex = result.size - 1
            var i = 0
            while (i < lastIndex) {
                result[i] = s.substring(j, j + interval)
                i++
                j += interval
            }
            result[lastIndex] = s.substring(j)
            return result
        }
    }
}