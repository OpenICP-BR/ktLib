package com.github.OpenICP_BR.ktLib

import org.bouncycastle.asn1.ASN1EncodableVector
import org.bouncycastle.asn1.cms.CMSAttributes
import org.bouncycastle.asn1.x500.X500Name
import org.bouncycastle.asn1.x509.X509Name
import org.bouncycastle.cert.jcajce.JcaCertStore
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder
import java.io.InputStream
import java.util.*
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder
import org.bouncycastle.cms.jcajce.JcaSignerInfoGeneratorBuilder
import java.security.cert.X509Certificate
import org.bouncycastle.asn1.DERUTCTime
import org.bouncycastle.asn1.ASN1UTCTime
import org.bouncycastle.asn1.cms.AttributeTable
import org.bouncycastle.asn1.cms.SignerInfo
import org.bouncycastle.cms.*
import java.lang.Exception
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoVerifierBuilder
import org.bouncycastle.cert.X509CertificateHolder
import org.bouncycastle.cms.SignerInformation
import org.bouncycastle.cms.SignerInformationStore
import org.bouncycastle.jcajce.PKIXCertStoreSelector.getCertificates
import org.bouncycastle.util.Selector


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

    fun verify(): Boolean {
        val signer: SignerInformation
        val cert: X509CertificateHolder

        try {
            signer = base.signerInfos[signerId]
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
}