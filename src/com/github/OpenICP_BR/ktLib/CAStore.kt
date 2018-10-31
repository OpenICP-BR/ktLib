package com.github.OpenICP_BR.ktLib

/**
 * Holds Certificate Authorities and validates certificates.
 */
class CAStore() {
    // cas = Certificate Authorities (note the plural)
    internal var cas : HashMap<String, Certificate> = HashMap()

    init {
        this.forceAddCA(getRootCert("v1"))
        this.forceAddCA(getRootCert("v2"))
        this.forceAddCA(getRootCert("v5"))
    }

    /**
     * Adds a CA without any validity check.
     */
    internal fun forceAddCA(cert: Certificate) {
        this.cas[cert.fullSubject] = cert
        if (cert.subjectKeyId != "") {
            this.cas[cert.subjectKeyId] = cert
        }
    }

    /**
     * Returns the issuer of a given certificate if it exists, or an exception if it does not.
     */
    internal fun getIssuer(cert: Certificate): Certificate {
        if (cert.authorityKeyId != "" && cert.authorityKeyId in this.cas) {
            return this.cas[cert.authorityKeyId]!!
        }
        if (cert.fullIssuer in this.cas) {
            return this.cas[cert.fullIssuer]!!
        }
        throw NoSuchElementException("cert.authorityKeyId = "+cert.authorityKeyId+" cert.fullIssuer = "+cert.fullIssuer)
    }

    /**
     * Generates a certification path from the root to the end certificate.
     */
    internal fun getPath(cert: Certificate): Array<Certificate> {
        var ans = arrayOf<Certificate>(cert)
        if (cert.isSelfSigned()) {
            ans += cert
            return ans
        }
        val issuer = getIssuer(cert)
        ans += getPath(issuer)
        return ans
    }

    /**
     * Verify if a given certificate was issued by a trusted CA and the not_before and not_after dates.
     */
    fun verifyCert(cert: Certificate) {
        val path = getPath(cert)

        // Implement verification logic
        for (i in 0..path.size-2) {
            path[i].base!!.verify(path[i + 1].base!!.publicKey)
            path[i].base!!.checkValidity()
        }
    }

    /**
     * The same as verifyCert(), but returns a boolean instead of exceptions.
     */
    fun verifyCertBool(cert: Certificate): Boolean {
        try {
            verifyCert(cert)
            return true
        } catch (e: Exception) {
            return false
        }
    }

    /**
     * Adds a CA to the trusted list if it is valid.
     */
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

    /**
     * Adds a testing root CA to the trusted list if it is has the proper subject and issuer.
     */
    fun addTestingCA(cert: Certificate): Boolean {
        if (!cert.isCA()) {
            return false
        }
        if (cert.fullSubject != TESTING_ROOT_CA_SUBJECT || cert.fullIssuer != TESTING_ROOT_CA_SUBJECT) {
            throw IllegalTestingRootCA()
        }

        forceAddCA(cert)
        return true
    }
}
