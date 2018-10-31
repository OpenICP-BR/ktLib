package com.github.OpenICP_BR.ktLib

import org.bouncycastle.asn1.ASN1Sequence
import org.bouncycastle.asn1.x500.X500Name
import org.bouncycastle.asn1.x500.X500NameBuilder
import org.bouncycastle.asn1.x509.X509Name
import org.bouncycastle.asn1.x509.X509NameEntryConverter
import java.util.*
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.Security
import javax.security.auth.x500.X500Principal

/**
 * A function that initializes this library. This MUST be called before anything else.
 */
fun ICPinit() {
    Security.addProvider(BouncyCastleProvider())
}

fun ByteArray.toHex() = this.joinToString(separator = "") { it.toInt().and(0xff).toString(16).padStart(2, '0') }

/**
 * A string representing the current version of this library.
 */
val ICPVersion: String
    get() {
        val properties = Properties()
        properties.load(Certificate::class.java.getResourceAsStream("info.properties"));
        return properties.getProperty("version")
    }

internal fun javaxX500Principal2BCX509Name(src: X500Principal): X500Name {
    return X500Name(src.getName("CANONICAL"))
}