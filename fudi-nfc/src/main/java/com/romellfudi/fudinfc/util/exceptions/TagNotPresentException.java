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
