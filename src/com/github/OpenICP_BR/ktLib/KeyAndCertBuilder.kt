package com.github.OpenICP_BR.ktLib

import org.bouncycastle.asn1.x500.X500Name
import org.bouncycastle.asn1.x500.X500NameBuilder
import org.bouncycastle.asn1.x500.style.BCStyle
import org.bouncycastle.asn1.x509.*
import org.bouncycastle.cert.X509ExtensionUtils
import org.bouncycastle.cert.X509v3CertificateBuilder
import org.bouncycastle.crypto.digests.SHA1Digest
import org.bouncycastle.jcajce.provider.digest.SHA1
import org.bouncycastle.operator.ContentSigner
import org.bouncycastle.operator.DigestCalculator
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder
import org.cryptacular.generator.KeyPairGenerator
import org.cryptacular.x509.ExtensionReader
import java.math.BigInteger
import java.security.KeyPair
import java.security.SecureRandom
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
import org.bouncycastle.asn1.oiw.OIWObjectIdentifiers
import org.bouncycastle.asn1.x509.AlgorithmIdentifier
import org.bouncycastle.operator.bc.BcDigestCalculatorProvider



class KeyAndCertBuilder {
    var isRoot: Boolean = false
    var isCA: Boolean = false
    var isTimestampingAuthority: Boolean = false
    var serial: BigInteger = BigInteger.ONE
    var issuer: KeyAndCert? = null
    val keyType: String = "RSA"
    var keyLen: Int = 2048
    var notBefore: Date
    var notAfter: Date
    var subjectCN: String = ""
    var subjectEmail: String = ""
    val subjectC: String
        get() = issuer?.cert?.getSubjectPart(BCStyle.C) ?: TESTING_ROOT_CA_SUBJECT_C
    val subjectO: String
        get() = issuer?.cert?.getSubjectPart(BCStyle.O) ?: TESTING_ROOT_CA_SUBJECT_O
    val subjectOU: String
        get() = issuer?.cert?.getSubjectPart(BCStyle.OU) ?: TESTING_ROOT_CA_SUBJECT_OU

    internal var keyPair: KeyPair? = null
    internal var subPubKeyInfo: SubjectPublicKeyInfo? = null

    init {
        val now = LocalDate.now()
        val end = now.plusYears(1)
        notBefore = Date.from(now.atStartOfDay(ZoneId.of("GMT")).toInstant())
        notAfter = Date.from(end.atStartOfDay(ZoneId.of("GMT")).toInstant())
    }

    internal fun generateKeyPair(){
        // Generate key pair
        keyPair = KeyPairGenerator.generateRSA(SecureRandom(), keyLen)
        subPubKeyInfo = SubjectPublicKeyInfo.getInstance(keyPair!!.public.encoded);
    }

    fun build(): KeyAndCert {
        // Safety checks
        if (!isRoot && issuer?.privateKey == null) {
            throw ICPException(
                    "ICP_NULL_ISSUER_FOR_NON_ROOT_CERTIFICATE",
                    "any non root certificate MUST have an issuer",
                    "todos os certificados não-raíz DEVEM ter um emissor"
            )
        }

        // Fix names
        val issuerName: X500Name
        val subjectNameBuilder = X500NameBuilder()
        if (isRoot) {
            subjectCN = TESTING_ROOT_CA_SUBJECT_CN
            issuerName = X500Name(TESTING_ROOT_CA_SUBJECT)
        } else {
            issuerName = issuer!!.cert.baseHolder.issuer
        }
        if (subjectC != "") subjectNameBuilder.addRDN(BCStyle.C, subjectC)
        if (subjectO != "") subjectNameBuilder.addRDN(BCStyle.O, subjectO)
        if (subjectOU != "") subjectNameBuilder.addRDN(BCStyle.OU, subjectOU)
        if (subjectCN != "") subjectNameBuilder.addRDN(BCStyle.CN, subjectCN)
        if (subjectEmail != "") subjectNameBuilder.addRDN(BCStyle.EmailAddress, subjectEmail)

        // Get our keys
        this.generateKeyPair()

        // Generate certificate
        val certGen = X509v3CertificateBuilder(
                issuerName,
                serial,
                notBefore,
                notAfter,
                subjectNameBuilder.build(),
                subPubKeyInfo)

        // Calculate and add key identifiers
        val digCalc = BcDigestCalculatorProvider().get(AlgorithmIdentifier(OIWObjectIdentifiers.idSHA1))
        val utils = X509ExtensionUtils(digCalc)
        val subjectKey = utils.createSubjectKeyIdentifier(subPubKeyInfo)
        certGen.addExtension(Extension.subjectKeyIdentifier, false, subjectKey.toASN1Primitive())

        var authorityKey = subjectKey.keyIdentifier
        if (issuer != null) {
            authorityKey = ExtensionReader(issuer!!.cert.base).readSubjectKeyIdentifier().keyIdentifier
        }
        certGen.addExtension(Extension.authorityKeyIdentifier, false, AuthorityKeyIdentifier(authorityKey).toASN1Primitive())

        // Set key usages
        var extendedUsages = arrayOf<KeyPurposeId>()
        if (isCA) {
            certGen.addExtension(Extension(
                    Extension.keyUsage,
                    true,
                    KeyUsage(KeyUsage.cRLSign or KeyUsage.keyCertSign).encoded))
            certGen.addExtension(Extension(
                    Extension.basicConstraints,
                    true,
                    BasicConstraints(1).encoded))
            extendedUsages += KeyPurposeId.id_kp_OCSPSigning
        } else {
            certGen.addExtension(Extension(
                    Extension.keyUsage,
                    true,
                    KeyUsage(KeyUsage.digitalSignature or KeyUsage.nonRepudiation or KeyUsage.keyEncipherment).encoded))
            certGen.addExtension(Extension(
                    Extension.basicConstraints,
                    true,
                    BasicConstraints(false).encoded))
            extendedUsages += KeyPurposeId.id_kp_clientAuth
            extendedUsages += KeyPurposeId.id_kp_emailProtection
            extendedUsages += KeyPurposeId.id_kp_scvpClient
            extendedUsages += KeyPurposeId.id_kp_smartcardlogon
        }
        if (isTimestampingAuthority) {
            extendedUsages += KeyPurposeId.id_kp_timeStamping
        }
        certGen.addExtension(Extension(
                Extension.extendedKeyUsage,
                true,
                ExtendedKeyUsage(extendedUsages).encoded))

        // Sign certificate
        val signer : ContentSigner
        if (isRoot) {
            // Self signed
            signer = JcaContentSignerBuilder("SHA512WithRSA").build(keyPair!!.private)
        } else {
            // Not self signed
            signer = JcaContentSignerBuilder("SHA512WithRSA").build(issuer!!.keyPair!!.private)
        }
        val cert = certGen.build(signer)

        //Finish
        return KeyAndCert(cert, keyPair!!.private)
    }
}