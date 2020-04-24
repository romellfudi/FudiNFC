
/*
 * Copyright (c) 2020. BoostTag E.I.R.L. Romell D.Z.
 * All rights reserved
 * porfile.romellfudi.com
 */

package com.romellfudi.fudinfc.util.constants;

public class NfcHead {

    public static final byte CUSTOM_SCHEME = 0x00;
    /**
     * This URI is in the format of http://www.
     */
    public static final byte HTTP_WWW = 0x01;
    /**
     * This URI is in the format of https://www.
     */
    public static final byte HTTPS_WWW = 0x02;
    /**
     * This URI is in the format of http://.
     */
    public static final byte HTTP = 0x03;
    /**
     * This URI is in the format of https://.
     */
    public static final byte HTTPS = 0x04;
    /**
     * This URI is in the format of tel:
     */
    public static final byte TEL = 0x05;
    /**
     * This URI is in the format of mailto:.
     */
    public static final byte MAILTO = 0x06;
    /**
     * This URI is in the format of ftp://anonymous:anonymous@
     */
    public static final byte FTP_ANONYMOUS = 0x07;
    /**
     * This URI is in the format of ftp://ftp.
     */
    public static final byte FTP_URI = 0x08;
    /**
     * This URI is in the format of ftps://
     */
    public static final byte FTPS = 0x09;
    /**
     * This URI is in the format of sftp://
     */
    public static final byte SFTP = 0x0A;
    /**
     * This URI is in the format of smb://
     */
    public static final byte SMB = 0x0B;
    /**
     * This URI is in the format of nfs://
     */
    public static final byte NFS = 0x0C;
    /**
     * This URI is in the format of ftp://
     */
    public static final byte FTP = 0x0D;
    /**
     * This URI is in the format of dav://
     */
    public static final byte DAV = 0x0E;
    /**
     * This URI is in the format of news://
     */
    public static final byte NEWS = 0x0F;
    /**
     * This URI is in the format of telnet://
     */
    public static final byte TELNET = 0x10;
    /**
     * This URI is in the format of imap:
     */
    public static final byte IMAP = 0x11;
    /**
     * This URI is in the format of rtsp://.
     */
    public static final byte RTSP = 0x12;
    /**
     * This URI is in the format of urn:
     */
    public static final byte URN = 0x13;
    /**
     * This URI is in the format of pop:
     */
    public static final byte POP = 0x14;
    /**
     * This URI is in the format of sip:
     */
    public static final byte SIP = 0x15;
    /**
     * This URI is in the format of sips:
     */
    public static final byte SIPS = 0x16;
    /**
     * This URI is in the format of tftp:
     */
    public static final byte TFTP = 0x17;
    /**
     * This URI is in the format of btspp://
     */
    public static final byte BT_SPP = 0x18;
    /**
     * This URI is in the format of bt12cap://.
     */
    public static final byte BT_12CAP = 0x19;
    /**
     * This URI is in the format of btgoep://.
     */
    public static final byte BT_GOEP = 0x1A;
    /**
     * This URI is in the format of tcpobex://
     */
    public static final byte TCP_OBEX = 0x1B;
    /**
     * This URI is in the format of irdaobex://
     */
    public static final byte IRDA_OBEX = 0x1C;
    /**
     * This URI is in the format of file://
     */
    public static final byte FILE = 0x1D;
    /**
     * This URI is in the format of urn:epc:id:
     */
    public static final byte URN_EPC_ID = 0x1E;
    /**
     * This URI is in the format of urn:epc:tag:
     */
    public static final byte URN_EPC_TAG = 0x1F;

    /**
     * This URI is in the format of urn:epc:pat:
     */
    public static final byte URN_EPC_PAT = 0x20;

    /**
     * This URI is in the format of urn:epc:raw:
     */
    public static final byte URN_EPC_RAW = 0x21;

    /**
     * This URI is in the format of urn:epc:
     */
    public static final byte URN_EPC = 0x22;

    /**
     * This URI is in the format of urn:nfc:
     */
    public static final byte URN_NFC = 0x23;

}
