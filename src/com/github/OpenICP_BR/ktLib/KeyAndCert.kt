package com.github.OpenICP_BR.ktLib

import org.bouncycastle.cert.X509CertificateHolder
import java.io.FileInputStream
import java.security.KeyStore
import java.security.PrivateKey
import java.security.cert.X509Certificate
import java.security.KeyPair

/**
 * Represents a .pfx or .p12 file. This is, a certificate and the key pair.
 */
class KeyAndCert {
    lateinit var cert : Certificate
        internal set
    var privateKey : PrivateKey? = null
    var keyPair : KeyPair? = null
        get() = KeyPair(this.cert.base!!.publicKey, this.privateKey)

    constructor(new_cert: X509CertificateHolder, new_key: PrivateKey) {
        val bytes = new_cert.toASN1Structure().toASN1Primitive().encoded
        this.cert = Certificate(bytes.inputStream())
        this.privateKey = new_key
    }

    constructor(new_cert: Certificate, new_key: PrivateKey) {
        this.cert = new_cert
        this.privateKey = new_key
    }

    fun hasKey(): Boolean {
        return this.privateKey != null
    }

    fun save(path: String, password: String) {

    }

    constructor(path: String, password: String) : this(path, password, password) {
    }

    constructor(path: String, password: String, password_key: String) {
        val p12 = KeyStore.getInstance("pkcs12")
        p12.load(FileInputStream(path), password.toCharArray())
        val e = p12.aliases()
        var got = false
        while (e.hasMoreElements()) {
            val alias = e.nextElement() as String
            val c = Certificate(p12.getCertificate(alias) as X509Certificate)
            this.cert = c
            val k = p12.getKey(alias, password_key.toCharArray())
            this.privateKey = k as PrivateKey
            got = true
            // We only need one certificate and key
            break
        }

        if (!got) {
            throw Exception("failed to find certificate and private key")
        }
    }
}
