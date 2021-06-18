/*
 * Copyright (c) 2020. BoostTag E.I.R.L. Romell D.Z.
 * All rights reserved
 * porfile.romellfudi.com
 */
package com.romellfudi.fudinfc.util.exceptions

class ReadOnlyTagException : TagNotWritableException {
    constructor() {}
    constructor(message: String?) : super(message) {}
}