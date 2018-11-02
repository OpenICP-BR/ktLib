package com.github.OpenICP_BR.ktLib

import org.bouncycastle.asn1.cms.CMSAttributes
import org.bouncycastle.asn1.DERUTCTime
import org.bouncycastle.asn1.ASN1UTCTime
import org.bouncycastle.cms.*
import java.lang.Exception
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoVerifierBuilder
import org.bouncycastle.cert.X509CertificateHolder
import org.bouncycastle.cms.SignerInformation
import org.bouncycastle.util.Selector
import java.io.File
import java.util.*

/**
 * Represents a single CAdES signature
 *
 * @property base the "raw" signature object used by Bouncy Castle
 * @property signerId identifies which signature we are talking about.
 */
class Signature(val base: CMSSignedData, val signerId: SignerId) {
    val date: Date
        get() {
            val attr = base.signerInfos[signerId].signedAttributes.get(CMSAttributes.signingTime)
            val en = attr.attrValues.objects
            while (en.hasMoreElements()) {
                val obj = en.nextElement()
                if (obj is ASN1UTCTime) {
                    return obj.date
                } else if (obj is DERUTCTime) {
                    return obj.date
                } else if (obj is org.bouncycastle.asn1.cms.Time) {
                    return obj.date
                }
            }
            throw ICPException(
                    "SIGNING_TIME_NOT_FOUND",
                    "this signature has no signing time",
                    "essa assinatura não tem a data de assinatura")
        }

    /**
     * Verifies if the integrity of this signature. This is, if the message has not been modified. No checks are made as to whether the certificate itself is valid or not.
     */
    fun verify(): Boolean {
        val signer: SignerInformation
        val cert: X509CertificateHolder


        try {
            if (base.signerInfos.size() == 1) {
                signer = base.signerInfos.signers.iterator().next()
            } else {
                signer = base.signerInfos[signerId]
            }
            cert = base.certificates.getMatches(signer.sid as Selector<X509CertificateHolder>).iterator().next()
        } catch (e: Exception) {
            throw FailedToGetCertificateFromSignatureException(e)
        }
        try {
            return signer.verify(JcaSimpleSignerInfoVerifierBuilder().setProvider("BC").build(cert))
        } catch (e: Exception) {
            throw FailedToVerifySignatureException(e)
        }
    }

    /**
     * The same as verify(),  but checks all signatures within this.base.
     */
    internal fun verifyAll(): Boolean {
        val certStore = base.certificates
        val signers = base.signerInfos
        val c = signers.signers
        val it = c.iterator()
        while (it.hasNext()) {
            val signer = it.next() as SignerInformation
            val certCollection = certStore.getMatches(signer.sid as Selector<X509CertificateHolder>)
            val certIt = certCollection.iterator()
            val cert = certIt.next() as X509CertificateHolder
            if (!signer.verify(JcaSimpleSignerInfoVerifierBuilder().setProvider("BC").build(cert))) {
                return false
            }
        }
        return true
    }

    fun save(path: String) {
        File(path).writeBytes(this.base.encoded)
    }
}