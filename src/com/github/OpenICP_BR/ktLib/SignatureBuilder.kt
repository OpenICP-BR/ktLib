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
import org.bouncycastle.cms.CMSProcessableByteArray
import jdk.internal.jimage.decompressor.StringSharingDecompressor.getEncoded
import org.bouncycastle.cms.SignerInformationStore
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder
import org.bouncycastle.cms.CMSSignedDataGenerator
import org.bouncycastle.cms.SignerInformation
import org.bouncycastle.operator.DigestCalculatorProvider
import org.bouncycastle.cms.CMSSignedData
import java.io.IOException
import org.bouncycastle.cms.CMSException
import java.security.GeneralSecurityException
import org.bouncycastle.operator.OperatorCreationException
import java.security.PrivateKey






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

    fun setMsg(msg: CMSSignedData) {
        msgArray = CMSProcessableByteArray(msg.encoded)
        this.counter = msg
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
    fun directSign(signer: KeyAndCert, attached: Boolean): Signature {
        val certBase = signer.cert.base!!
        val gen = CMSSignedDataGenerator()
        val hashSigner = JcaContentSignerBuilder(this.alg).setProvider("BCFIPS").build(signer.privateKey);
        val builder = JcaSignerInfoGeneratorBuilder(JcaDigestCalculatorProviderBuilder().setProvider("BCFIPS").build())

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

    fun counterSign(
            oldSigningCert: X509Certificate,
            data: ByteArray, newSignerKey: PrivateKey, newSignerCert: X509Certificate): ByteArray {

        var signer = this.counter!!.signerInfos.iterator().next()


        val counterSignerGen = CMSSignedDataGenerator()
        val digProvider = JcaDigestCalculatorProviderBuilder()
                .setProvider("BCFIPS").build()
        val signerInfoGeneratorBuilder = JcaSignerInfoGeneratorBuilder(digProvider)
        counterSignerGen.addSignerInfoGenerator(signerInfoGeneratorBuilder.build(
                JcaContentSignerBuilder(this.alg)
                        .setProvider("BCFIPS").build(newSignerKey),
                newSignerCert))
        val counterSigners = counterSignerGen.generateCounterSigners(signer)
        signer = SignerInformation.addCounterSigners(signer, counterSigners)
        val signerGen = CMSSignedDataGenerator()
        signerGen.addCertificate(JcaX509CertificateHolder(oldSigningCert))
        signerGen.addCertificate(JcaX509CertificateHolder(newSignerCert))
        signerGen.addSigners(SignerInformationStore(signer))
        return signerGen.generate(CMSProcessableByteArray(data), true).encoded
    }
}