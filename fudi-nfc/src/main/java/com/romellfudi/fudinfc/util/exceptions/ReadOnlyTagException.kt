/*
 * Copyright (c) 2020. BoostTag E.I.R.L. Romell D.Z.
 * All rights reserved
 * porfile.romellfudi.com
 */

package com.romellfudi.fudinfc.util.exceptions;

public class ReadOnlyTagException extends TagNotWritableException {

    public ReadOnlyTagException() {
    }

    public ReadOnlyTagException(String message) {
        super(message);
    }


}
