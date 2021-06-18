/*
 * Copyright (c) 2020. BoostTag E.I.R.L. Romell D.Z.
 * All rights reserved
 * porfile.romellfudi.com
 */

package com.romellfudi.fudinfc.util.exceptions;

public class InsufficientCapacityException extends Exception {

    public InsufficientCapacityException() {
    }

    public InsufficientCapacityException(String message) {
        super(message);
    }

    public InsufficientCapacityException(StackTraceElement[] stackTraceElements) {
        setStackTrace(stackTraceElements);
    }
}
