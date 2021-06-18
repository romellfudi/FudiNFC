/*
 * Copyright (c) 2020. BoostTag E.I.R.L. Romell D.Z.
 * All rights reserved
 * porfile.romellfudi.com
 */
package com.romellfudi.fudinfc.util.exceptions

class TagNotPresentException : Exception {
    constructor() : super("Intent does not contain a tag") {}
    constructor(message: String?) : super(message) {}
    constructor(e: Exception) {
        stackTrace = e.stackTrace
    }
}