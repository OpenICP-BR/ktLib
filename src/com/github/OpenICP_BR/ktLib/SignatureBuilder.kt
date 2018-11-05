package com.github.OpenICP_BR.ktLib

import org.bouncycastle.asn1.ASN1EncodableVector
import org.bouncycastle.asn1.DERSet
import org.bouncycastle.asn1.DERUTF8String
import org.bouncycastle.asn1.cms.AttributeTable
import org.bouncycastle.asn1.esf.SignerLocation
import org.bouncycastle.asn1.pkcs.Attribute
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers
import org.bouncycastle.cert.jcajce.JcaCertStore
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder
import org.bouncycastle.cms.*
import org.bouncycastle.cms.SignerInformation
import org.bouncycastle.cms.jcajce.JcaSignerInfoGeneratorBuilder
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder
import java.io.InputStream
import java.security.PrivateKey
import java.security.cert.X509Certificate


/**
 * Generates CAdES signatures.
 */
class SignatureBuilder {
    internal var counter : CMSSignedData? = null
    internal var msgArray : CMSProcessableByteArray? = null
    internal var signedAttrs : ASN1EncodableVector? = null
    var alg : String = "SHA256withRSA"

    /**
     * Sets what message we want to sign.
     * @param msg the message to be signed.
     */
    fun setMsg(msg: ByteArray) {
        msgArray = CMSProcessableByteArray(msg)
    }

    /**
     * Sets what message we want to sign.
     * @param msg the message to be signed.
     */
    fun setMsg(msg: InputStream) {
        msgArray = CMSProcessableByteArray(msg.readAllBytes())
    }

    /**
     * Sets what message we want to sign.
     * @param msg the message to be signed.
     */
    fun setMsg(msg: String) {
        msgArray = CMSProcessableByteArray(msg.toByteArray(Charsets.UTF_8))
    }

    fun setMsg(msg: Signature) {
        msgArray = CMSProcessableByteArray(msg.base.encoded)
        this.counter = msg.base
    }

    /**
     * Sets the physical location of the signer. (optional signed attribute)
     * @param city the name of the city/município where the signer is.
     * @param state the abbreviation of state where the signer is.
     * @param country the ISO 3166-1 numeric code the country where the signer is.
     */
    fun setSignerLocation(city: String, state : String, country : Int) {
        setSignerLocation("$city-$state", country)
    }

    /**
     * Sets the physical location of the signer. (optional signed attribute)
     * @param locality the name of the place where the signer is.
     * @param country the ISO 3166-1 numeric code the country where the signer is.
     */
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

    private fun ensureSignedAttrs() {
        if (signedAttrs == null) {
            signedAttrs = ASN1EncodableVector()
        }
    }
    
    

    /**
     * Signs the message and the attributes given. The signing time is added automatically.
     *
     * @param signer the certificate and private key of the signer.
     * @param attached if true, the message being signed will be included in the output.
     */
    internal fun directSign(signer: KeyAndCert, attachMessage: Boolean): Signature {
        val certBase = signer.cert.base!!
        val gen = CMSSignedDataGenerator()
        val hashSigner = JcaContentSignerBuilder(this.alg).setProvider("BC").build(signer.privateKey);
        val builder = JcaSignerInfoGeneratorBuilder(JcaDigestCalculatorProviderBuilder().setProvider("BC").build())

        if (signedAttrs != null) {
            builder.setSignedAttributeGenerator(DefaultSignedAttributeTableGenerator(AttributeTable(signedAttrs)))
        }

        gen.addSignerInfoGenerator(builder.build(hashSigner, signer.cert.base))
        val certs = ArrayList<X509Certificate>()
        certs.add(certBase)
        gen.addCertificates(JcaCertStore(certs));

        return Signature(
                gen.generate(msgArray, attachMessage),
                SignerId(
                        javaxX500Principal2BCX509Name(certBase.issuerX500Principal),
                        certBase.serialNumber
                ))
    }

    internal fun counterSign(
            oldSigningCert: X509Certificate?,
            data: CMSProcessableByteArray,
            newSignerKey: PrivateKey,
            newSignerCert: X509Certificate,
            attachMessage: Boolean): Signature {

        var signer = this.counter!!.signerInfos.iterator().next()


        val counterSignerGen = CMSSignedDataGenerator()
        val digProvider = JcaDigestCalculatorProviderBuilder()
                .setProvider("BC").build()
        val signerInfoGeneratorBuilder = JcaSignerInfoGeneratorBuilder(digProvider)
        counterSignerGen.addSignerInfoGenerator(signerInfoGeneratorBuilder.build(
                JcaContentSignerBuilder(this.alg)
                        .setProvider("BC").build(newSignerKey),
                newSignerCert))
        val counterSigners = counterSignerGen.generateCounterSigners(signer)
        signer = SignerInformation.addCounterSigners(signer, counterSigners)
        val gen = CMSSignedDataGenerator()
        if (oldSigningCert != null) {
            gen.addCertificate(JcaX509CertificateHolder(oldSigningCert))
        }
        gen.addCertificate(JcaX509CertificateHolder(newSignerCert))
        gen.addSigners(SignerInformationStore(signer))
        gen.generate(data, true).encoded

        return Signature(
                gen.generate(msgArray, attachMessage),
                SignerId(
                        javaxX500Principal2BCX509Name(newSignerCert.issuerX500Principal),
                        newSignerCert.serialNumber
                ))
    }

    fun finish(signer: KeyAndCert, attachMessage: Boolean): Signature {
        if (this.counter == null) {
            return directSign(signer, attachMessage)
        } else {
            return counterSign(null, msgArray!!, signer.privateKey!!, signer.cert.base!!, attachMessage)
        }
    }
}