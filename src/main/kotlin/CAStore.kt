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

class CAStore() {
    internal var cas : HashMap<String, Certificate> = HashMap()

    init {
        this.forceAddCA(getRootCert("v1"))
        this.forceAddCA(getRootCert("v2"))
        this.forceAddCA(getRootCert("v5"))
    }

    internal fun forceAddCA(cert: Certificate) {
        this.cas[cert.fullSubject] = cert
        if (cert.subjectKeyId != "") {
            this.cas[cert.subjectKeyId] = cert
        }
    }

    internal fun getIssuer(cert: Certificate): Certificate {
        if (cert.authorityKeyId != "" && cert.authorityKeyId in this.cas) {
            return this.cas[cert.authorityKeyId]!!
        }
        if (cert.fullIssuer in this.cas) {
            return this.cas[cert.fullIssuer]!!
        }
        throw NoSuchElementException("cert.authorityKeyId = "+cert.authorityKeyId+" cert.fullIssuer = "+cert.fullIssuer)
    }

    internal fun getPath(cert: Certificate): Array<Certificate> {
        var ans = arrayOf<Certificate>(cert)
        if (cert.isSelfSigned()) {
            ans += cert
            return ans
        }
        var issuer = getIssuer(cert)
        ans += getPath(issuer)
        return ans
    }

    fun verifyCert(cert: Certificate) {
        var path = getPath(cert)

        // Implement verification logic
        for (i in 0..path.size-2) {
            path[i].base!!.verify(path[i + 1].base!!.publicKey)
        }
    }

    fun verifyCertBool(cert: Certificate): Boolean {
        try {
            verifyCert(cert)
            return true
        } catch (e: Exception) {
            return false
        }
    }

    fun addCA(cert: Certificate): Boolean {
        if (!cert.isCA()) {
            return false
        }
        if (!this.verifyCertBool(cert)) {
            return false
        }

        forceAddCA(cert)
        return true
    }

    fun addTestingCA(cert: Certificate): Boolean {
        if (!cert.isCA()) {
            return false
        }
        if (cert.fullSubject != TESTING_ROOT_CA_SUBJECT || cert.fullIssuer != TESTING_ROOT_CA_SUBJECT) {
            throw IllegalArgumentException("Testing CAs MUST be: "+ TESTING_ROOT_CA_SUBJECT)
        }

        forceAddCA(cert)
        return true
    }
}
