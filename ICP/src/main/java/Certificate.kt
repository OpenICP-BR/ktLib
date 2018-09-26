import org.cryptacular.util.CertUtil
import org.cryptacular.x509.KeyUsageBits
import org.cryptacular.x509.dn.NameReader
import org.cryptacular.x509.dn.StandardAttributeType
import java.io.File
import java.security.cert.X509Certificate
import java.util.*

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

class Certificate() {
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
    internal var base: X509Certificate? = null
        internal set
    var subjectKeyId: String = ""
        internal set
    var authorityKeyId: String = ""
        internal set
    var fullSubject: String = ""
        internal set
    var fullIssuer: String = ""
        internal set

    constructor(path: String) : this() {
        this.loadFromFile(path)
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
            this.personId = String.format("%s.%s.%s-%s", doc.substring(0, 3), doc.substring(3, 6), doc.substring(6,
                    9), doc.substring(9))
        }
        // Special formatting for CNPJ
        if (doc.matches("[0-9]{14}".toRegex())) {
            this.personId = String.format("%s.%s.%s/%s-%s", doc.substring(0, 2), doc.substring(2, 5), doc.substring(5,
                    8), doc.substring(8, 12), doc.substring(12))
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
        this.base = CertUtil.readCertificate(File(path).inputStream())
        if (this.base == null) {
            return
        }
        this.parseName(CertUtil.subjectCN(this.base))
        this.notAfter = this.base!!.notAfter!!
        this.notBefore = this.base!!.notBefore!!
        this.issuerName = NameReader(this.base).readIssuer().getValue(StandardAttributeType.CommonName)
        this.fullSubject = NameReader(this.base).readIssuer().toString()
        this.fullIssuer = NameReader(this.base).readIssuer().toString()
        try {
            this.subjectKeyId = CertUtil.subjectKeyId(this.base).toUpperCase()
        } catch (e: Exception) {
            this.subjectKeyId = this.issuerName + "-" + this.base!!.serialNumber.toString(10)
        }
        try {
            this.authorityKeyId = CertUtil.authorityKeyId(this.base).toUpperCase()
        } catch (e: Exception) {
            this.authorityKeyId = this.issuerName
        }
    }
}
