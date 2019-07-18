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
