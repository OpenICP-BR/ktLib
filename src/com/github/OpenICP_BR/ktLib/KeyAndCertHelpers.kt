package com.github.OpenICP_BR.ktLib

import org.bouncycastle.asn1.x500.X500Name
import org.bouncycastle.asn1.x509.*
import org.bouncycastle.cert.X509v3CertificateBuilder
import org.bouncycastle.operator.ContentSigner
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder
import org.cryptacular.generator.KeyPairGenerator
import java.math.BigInteger
import java.security.KeyPair
import java.security.SecureRandom
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

val TESTING_ROOT_CA_SUBJECT = "C=BR, CN=FakeICP - SEM VALOR LEGAL"

/**
 * Generates a new root CA for testing proposes which will be valid from now to one year from now.
 */
fun newTestRootCA(): KeyAndCert {
    val now = LocalDate.now()
    val end = now.plusYears(1)
    return newTestRootCA(
            Date.from(now.atStartOfDay(ZoneId.of("GMT")).toInstant()),
            Date.from(end.atStartOfDay(ZoneId.of("GMT")).toInstant()))
}

/**
 * Generates a new root CA for testing proposes.
 *
 * @param not_before the date in which the CA starts to be valid.
 * @param not_after the date in which the CA expires.
 */
fun newTestRootCA(not_before: Date, not_after: Date): KeyAndCert {
    return newCert(TESTING_ROOT_CA_SUBJECT, TESTING_ROOT_CA_SUBJECT, null, true, false, not_before, not_after)
}

/**
 * Generates a new end-user certificate.
 *
 * @param subject the name of the certificate subject.
 * @param issuer the certificate and private key of the issuer.
 * @param not_before the date in which the CA starts to be valid.
 * @param not_after the date in which the CA expires.
 */
fun newCert(subject: String, issuer: KeyAndCert, not_before: Date, not_after: Date):
        KeyAndCert {
    return newCert(subject, issuer.cert.fullSubject, issuer.keyPair, false, false, not_before, not_after)
}

/**
 * Generates a new timestamping authority certificate.
 *
 * @param subject the name of the certificate subject.
 * @param issuer the certificate and private key of the issuer.
 * @param not_before the date in which the CA starts to be valid.
 * @param not_after the date in which the CA expires.
 */
fun newTimeStampAuthority(subject: String, issuer: KeyAndCert, not_before: Date, not_after: Date):
        KeyAndCert {
    return newCert(subject, issuer.cert.fullSubject, issuer.keyPair, false, true, not_before, not_after)
}

/**
 * Generates a new certificate with a 2048-bit RSA key pair.
 *
 * @param subject the name of the certificate subject.
 * @param issuer the name of the certificate issuer.
 * @param issuerKeyPair the issuer key pair.
 * @param ca if true, the certificate will be able to sign other certificates (i.e. be a CA).
 * @param timestamp if true, the certificate will be allowed to sign timestamps.
 * @param not_before the date in which the CA starts to be valid.
 * @param not_after the date in which the CA expires.
 */
fun newCert(subject: String, issuer: String, issuerKeyPair: KeyPair?, ca: Boolean, timestamp: Boolean, not_before:
Date, not_after: Date):
        KeyAndCert {
    // Generate key pair
    val keyPair = KeyPairGenerator.generateRSA(SecureRandom(), 2048)
    val subPubKeyInfo = SubjectPublicKeyInfo.getInstance(keyPair.public.encoded);

    // Generate certificate
    val certGen = X509v3CertificateBuilder(
            X500Name(issuer),
            BigInteger.ONE,
            not_before,
            not_after,
            X500Name(subject),
            subPubKeyInfo)

    // This is a CA
    var extendedUsages = arrayOf<KeyPurposeId>()
    if (ca) {
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
    if (timestamp) {
        extendedUsages += KeyPurposeId.id_kp_timeStamping
    }
    certGen.addExtension(Extension(
            Extension.extendedKeyUsage,
            true,
            ExtendedKeyUsage(extendedUsages).encoded))

    // Sign certificate
    val signer : ContentSigner
    if (issuerKeyPair != null) {
        // Not self signed
        signer = JcaContentSignerBuilder("SHA1WithRSA").build(issuerKeyPair.private)
    } else {
        // Self signed
        signer = JcaContentSignerBuilder("SHA1WithRSA").build(keyPair.private)
    }
    val cert = certGen.build(signer)

    //Finish
    return KeyAndCert(cert, keyPair.private)
}
