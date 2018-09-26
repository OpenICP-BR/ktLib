import java.security.KeyStore

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

class CAStore() {
    // The lack of a password is not a problem, as the keystore should remain always in memory
    internal var protection = KeyStore.PasswordProtection("".toCharArray())
    internal var store : KeyStore

    init {
        this.store = KeyStore.Builder.newInstance("PKCS12", null, this.protection).keyStore
        this.forceAddCA(getRootCert("v1"))
        this.forceAddCA(getRootCert("v2"))
        this.forceAddCA(getRootCert("v5"))
    }

    internal fun forceAddCA(cert: Certificate) {
        var entry = KeyStore.TrustedCertificateEntry(cert.base)
        this.store.setEntry(cert.subjectAliasId, entry, protection)
        System.out.println(this.store.size())
    }
}
