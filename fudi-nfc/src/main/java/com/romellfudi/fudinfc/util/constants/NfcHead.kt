/*
 * Copyright (c) 2020. BoostTag E.I.R.L. Romell D.Z.
 * All rights reserved
 * porfile.romellfudi.com
 */
package com.romellfudi.fudinfc.util.constants

object NfcHead {
    const val CUSTOM_SCHEME: Byte = 0x00

    /**
     * This URI is in the format of http://www.
     */
    const val HTTP_WWW: Byte = 0x01

    /**
     * This URI is in the format of https://www.
     */
    const val HTTPS_WWW: Byte = 0x02

    /**
     * This URI is in the format of http://.
     */
    const val HTTP: Byte = 0x03

    /**
     * This URI is in the format of https://.
     */
    const val HTTPS: Byte = 0x04

    /**
     * This URI is in the format of tel:
     */
    const val TEL: Byte = 0x05

    /**
     * This URI is in the format of mailto:.
     */
    const val MAILTO: Byte = 0x06

    /**
     * This URI is in the format of ftp://anonymous:anonymous@
     */
    const val FTP_ANONYMOUS: Byte = 0x07

    /**
     * This URI is in the format of ftp://ftp.
     */
    const val FTP_URI: Byte = 0x08

    /**
     * This URI is in the format of ftps://
     */
    const val FTPS: Byte = 0x09

    /**
     * This URI is in the format of sftp://
     */
    const val SFTP: Byte = 0x0A

    /**
     * This URI is in the format of smb://
     */
    const val SMB: Byte = 0x0B

    /**
     * This URI is in the format of nfs://
     */
    const val NFS: Byte = 0x0C

    /**
     * This URI is in the format of ftp://
     */
    const val FTP: Byte = 0x0D

    /**
     * This URI is in the format of dav://
     */
    const val DAV: Byte = 0x0E

    /**
     * This URI is in the format of news://
     */
    const val NEWS: Byte = 0x0F

    /**
     * This URI is in the format of telnet://
     */
    const val TELNET: Byte = 0x10

    /**
     * This URI is in the format of imap:
     */
    const val IMAP: Byte = 0x11

    /**
     * This URI is in the format of rtsp://.
     */
    const val RTSP: Byte = 0x12

    /**
     * This URI is in the format of urn:
     */
    const val URN: Byte = 0x13

    /**
     * This URI is in the format of pop:
     */
    const val POP: Byte = 0x14

    /**
     * This URI is in the format of sip:
     */
    const val SIP: Byte = 0x15

    /**
     * This URI is in the format of sips:
     */
    const val SIPS: Byte = 0x16

    /**
     * This URI is in the format of tftp:
     */
    const val TFTP: Byte = 0x17

    /**
     * This URI is in the format of btspp://
     */
    const val BT_SPP: Byte = 0x18

    /**
     * This URI is in the format of bt12cap://.
     */
    const val BT_12CAP: Byte = 0x19

    /**
     * This URI is in the format of btgoep://.
     */
    const val BT_GOEP: Byte = 0x1A

    /**
     * This URI is in the format of tcpobex://
     */
    const val TCP_OBEX: Byte = 0x1B

    /**
     * This URI is in the format of irdaobex://
     */
    const val IRDA_OBEX: Byte = 0x1C

    /**
     * This URI is in the format of file://
     */
    const val FILE: Byte = 0x1D

    /**
     * This URI is in the format of urn:epc:id:
     */
    const val URN_EPC_ID: Byte = 0x1E

    /**
     * This URI is in the format of urn:epc:tag:
     */
    const val URN_EPC_TAG: Byte = 0x1F

    /**
     * This URI is in the format of urn:epc:pat:
     */
    const val URN_EPC_PAT: Byte = 0x20

    /**
     * This URI is in the format of urn:epc:raw:
     */
    const val URN_EPC_RAW: Byte = 0x21

    /**
     * This URI is in the format of urn:epc:
     */
    const val URN_EPC: Byte = 0x22

    /**
     * This URI is in the format of urn:nfc:
     */
    const val URN_NFC: Byte = 0x23
}