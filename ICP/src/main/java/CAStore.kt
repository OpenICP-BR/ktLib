import java.security.KeyStore
import javax.net.ssl.TrustManager
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

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
    }

    fun addCA(cert: Certificate): Boolean {
        var entry = KeyStore.TrustedCertificateEntry(cert.base)
//        var alias = cert.
//        this.store.setEntry("Root-V1", entry)


        return false
    }
}
