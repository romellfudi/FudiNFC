/*
 * Copyright (c) 2020. BoostTag E.I.R.L. Romell D.Z.
 * All rights reserved
 * porfile.romellfudi.com
 */
package com.romellfudi.fudinfc.util.exceptions

class InsufficientCapacityException : Exception {
    constructor() {}
    constructor(message: String?) : super(message) {}
    constructor(stackTraceElements: Array<StackTraceElement?>?) {
        stackTrace = stackTraceElements
    }
}