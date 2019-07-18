package com.romellfudi.fudinfc.util.exceptions;

public class ReadOnlyTagException extends TagNotWritableException {

    public ReadOnlyTagException() {
    }

    public ReadOnlyTagException(String message) {
        super(message);
    }


}
