package com.github.OpenICP_BR.ICP
/*
 * Copyright (c) 2018 G. Queiroz.
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * This program is distributed in the hope that it will be useful,  but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

import org.bouncycastle.cert.X509CertificateHolder
import java.io.FileInputStream
import java.security.KeyStore
import java.security.PrivateKey
import java.security.cert.X509Certificate
import java.security.KeyPair


class KeyAndCert {
    lateinit var cert : Certificate
        internal set
    internal var privateKey : PrivateKey? = null
    internal var keyPair : KeyPair? = null
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
