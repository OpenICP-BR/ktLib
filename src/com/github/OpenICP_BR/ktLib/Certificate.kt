package com.github.OpenICP_BR.ktLib

import org.bouncycastle.asn1.ASN1ObjectIdentifier
import org.bouncycastle.asn1.x500.style.BCStyle
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder
import org.cryptacular.util.CertUtil
import org.cryptacular.x509.KeyUsageBits
import org.cryptacular.x509.dn.NameReader
import org.cryptacular.x509.dn.StandardAttributeType
import java.io.File
import java.io.InputStream
import java.security.cert.X509Certificate
import java.util.*

class Certificate() {
    var serial: String = ""
        internal set
    var subjectName: String = ""
        internal set
    var issuerName: String = ""
        internal set
    var personId: String = ""
        internal set
    var notBefore: Date = Date(0)
        internal set
    var notAfter: Date = Date(0)
        internal set
    var subjectKeyId: String = ""
        internal set
    var authorityKeyId: String = ""
        internal set
    var fullSubject: String = ""
        internal set
    var fullIssuer: String = ""
        internal set
    internal var base: X509Certificate? = null
    val baseHolder: JcaX509CertificateHolder
    get() = JcaX509CertificateHolder(base)

    constructor(raw_cert: X509Certificate) : this() {
        this.base = raw_cert
        this.finishParsing()
    }

    constructor(path: String) : this() {
        this.loadFromFile(path)
    }

    constructor(stream: InputStream) : this() {
        this.loadFromStream(stream)
    }

    internal fun parseName(str: String) {
        val parts = str.split(":".toRegex(), 2).toTypedArray()
        this.subjectName = parts[0].trim()
        if (parts.size < 2) {
            return
        }
        this.personId = parts[1].trim()
        val doc = this.personId

        // Special formatting for CPF
        if (doc.matches("[0-9]{11}".toRegex())) {
            this.personId = String.format("%s.%s.%s-%s",
                    doc.substring(0, 3),
                    doc.substring(3, 6),
                    doc.substring(6, 9),
                    doc.substring(9))
        }
        // Special formatting for CNPJ
        if (doc.matches("[0-9]{14}".toRegex())) {
            this.personId = String.format("%s.%s.%s/%s-%s",
                    doc.substring(0, 2),
                    doc.substring(2, 5),
                    doc.substring(5, 8),
                    doc.substring(8, 12),
                    doc.substring(12))
        }
    }

    fun isSelfSigned(): Boolean {
        if (this.base == null) {
            return false
        } else {
            return this.base!!.subjectDN.equals(this.base!!.issuerDN)
        }
    }

    fun isCA(): Boolean {
        return this.base != null && CertUtil.allowsUsage(this.base, KeyUsageBits.KeyCertSign)
    }

    fun loadFromFile(path: String) {
        this.loadFromStream(File(path).inputStream())
    }

    fun loadFromStream(stream: InputStream) {
        try {
            this.base = CertUtil.readCertificate(stream)
            if (this.base == null) {
                return
            }
        } catch (e : Exception) {
            throw FailedToParseCertificateException(e)
        }
        finishParsing()
    }

    internal fun finishParsing() {
        try {
            this.parseName(CertUtil.subjectCN(this.base))
            this.notAfter = this.base!!.notAfter!!
            this.notBefore = this.base!!.notBefore!!
            this.issuerName = NameReader(this.base).readIssuer().getValue(StandardAttributeType.CommonName)
            this.fullSubject = NameReader(this.base).readSubject().toString()
            this.fullIssuer = NameReader(this.base).readIssuer().toString()
            try {
                this.subjectKeyId = CertUtil.subjectKeyId(this.base).toUpperCase()
            } catch (e: Exception) {
                this.subjectKeyId = ""
            }
            try {
                this.serial = this.base!!.serialNumber.toString(10)
            } catch (e: Exception) {
                this.serial = ""
            }
            try {
                this.authorityKeyId = CertUtil.authorityKeyId(this.base).toUpperCase()
            } catch (e: Exception) {
                this.authorityKeyId = ""
            }
        } catch (e : Exception) {
            throw FailedToParseCertificateException(e)
        }
    }

    internal fun getSubjectPart(part: ASN1ObjectIdentifier): String? {
        try {
            val base = this.baseHolder.subject!!.getRDNs(part)
            return base[0]!!.first!!.value.toString()
        } catch (e: Exception) {
            return null
        }
    }
}
