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

import java.io.FileInputStream
import java.security.KeyStore
import java.security.PrivateKey
import java.security.cert.X509Certificate

class KeyAndCert {
    lateinit var cert : Certificate
        internal set
    internal var key : PrivateKey? = null

    constructor(new_cert: Certificate, new_key: PrivateKey) {
        this.cert = new_cert
        this.key = new_key
    }

    constructor(path: String, password: String) : this(path, password, password) {
    }

    constructor(path: String, password: String, password_key: String) {
        var p12 = KeyStore.getInstance("pkcs12")
        p12.load(FileInputStream(path), password.toCharArray())
        var e = p12.aliases()
        var got = false
        while (e.hasMoreElements()) {
            val alias = e.nextElement() as String
            val c = Certificate(p12.getCertificate(alias) as X509Certificate)
            this.cert = c
            val k = p12.getKey(alias, password_key.toCharArray())
            this.key = k as PrivateKey
            got = true
            // We only need one certificate and key
            break
        }

        if (!got) {
            throw Exception("not certificate and private key found")
        }
    }
}