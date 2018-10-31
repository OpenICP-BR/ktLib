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
import org.bouncycastle.cms.*
import java.lang.Exception


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
}