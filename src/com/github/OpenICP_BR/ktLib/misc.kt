package com.github.OpenICP_BR.ktLib

import java.util.*

fun ByteArray.toHex() = this.joinToString(separator = "") { it.toInt().and(0xff).toString(16).padStart(2, '0') }

val ICPVersion: String
    get() {
        val properties = Properties()
        properties.load(Certificate::class.java.getResourceAsStream("/info.properties"));
        return properties.getProperty("version")
    }