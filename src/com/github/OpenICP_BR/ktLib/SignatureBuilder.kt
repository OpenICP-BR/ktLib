package com.github.OpenICP_BR.ktLib

import org.bouncycastle.asn1.*
import org.bouncycastle.asn1.cms.AttributeTable
import org.bouncycastle.asn1.esf.SignerLocation
import org.bouncycastle.asn1.pkcs.Attribute
import java.io.InputStream
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers
import org.bouncycastle.cert.jcajce.JcaCertStore
import org.bouncycastle.cms.*
import org.bouncycastle.cms.jcajce.JcaSignerInfoGeneratorBuilder
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder
import java.security.cert.X509Certificate


class SignatureBuilder {
    internal var msgArray : CMSProcessableByteArray? = null
    internal var signedAttrs : ASN1EncodableVector? = null
    var alg : String = "SHA256withRSA"

    fun setMsg(msg: ByteArray) {
        msgArray = CMSProcessableByteArray(msg)
    }

    fun setMsg(msg: InputStream) {
        msgArray = CMSProcessableByteArray(msg.readAllBytes())
    }

    fun setMsg(msg: String) {
        msgArray = CMSProcessableByteArray(msg.toByteArray(Charsets.UTF_8))
    }

    fun setSignerLocation(city: String, state : String, country : Int) {
        setSignerLocation("$city-$state", country)
    }

    fun setSignerLocation(locality: String, country: Int) {
        ensureSignedAttrs()
        val loc = SignerLocation(
                DERUTF8String(country.toString()),
                DERUTF8String(locality),
                null)
        val attr = Attribute(
                PKCSObjectIdentifiers.id_aa_ets_signerLocation,
                DERSet(loc.toASN1Primitive()))
        signedAttrs!!.add(attr)
    }

    internal fun ensureSignedAttrs() {
        if (signedAttrs == null) {
            signedAttrs = ASN1EncodableVector()
        }
    }

    fun finish(signer: KeyAndCert, attached: Boolean): Signature {
        val certBase = signer.cert.base!!
        val gen = CMSSignedDataGenerator()
        val hashSigner = JcaContentSignerBuilder(alg).setProvider("BC").build(signer.privateKey);
        val builder = JcaSignerInfoGeneratorBuilder(JcaDigestCalculatorProviderBuilder().setProvider("BC").build())

        if (signedAttrs != null) {
            builder.setSignedAttributeGenerator(DefaultSignedAttributeTableGenerator(AttributeTable(signedAttrs)))
        }

        gen.addSignerInfoGenerator(builder.build(hashSigner, signer.cert.base))
        val certs = ArrayList<X509Certificate>()
        certs.add(certBase)
        gen.addCertificates(JcaCertStore(certs));

        return Signature(
                gen.generate(msgArray, attached),
                SignerId(
                        javaxX500Principal2BCX509Name(certBase.issuerX500Principal),
                        certBase.serialNumber
                ))
    }
}