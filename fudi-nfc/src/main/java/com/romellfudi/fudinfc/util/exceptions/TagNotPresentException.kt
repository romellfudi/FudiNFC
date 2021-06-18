/*
 * Copyright (c) 2020. BoostTag E.I.R.L. Romell D.Z.
 * All rights reserved
 * porfile.romellfudi.com
 */

package com.romellfudi.fudinfc.util.exceptions;

public class TagNotPresentException extends Exception {

    public TagNotPresentException(){
        super("Intent does not contain a tag");
    }

    public TagNotPresentException(String message){
        super(message);
    }

    public TagNotPresentException(Exception e){
        this.setStackTrace(e.getStackTrace());
    }
}
