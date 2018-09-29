import org.bouncycastle.asn1.x500.X500Name
import org.bouncycastle.asn1.x509.*
import org.bouncycastle.cert.X509v3CertificateBuilder
import org.bouncycastle.operator.ContentSigner
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder
import org.cryptacular.generator.KeyPairGenerator
import java.math.BigInteger
import java.security.KeyPair
import java.security.SecureRandom
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


val TESTING_ROOT_CA_SUBJECT = "C=BR, CN=FakeICP - SEM VALOR LEGAL"

fun newTestRootCA(not_before: Date, not_after: Date): KeyAndCert {
    return newCert(TESTING_ROOT_CA_SUBJECT, TESTING_ROOT_CA_SUBJECT, null, true, false, not_before, not_after)
}

fun newCert(subject: String, issuer: KeyAndCert, not_before: Date, not_after: Date):
        KeyAndCert {
    return newCert(subject, issuer.cert.fullSubject, issuer.keyPair, false, false, not_before, not_after)
}

fun newTimeStampAuthority(subject: String, issuer: KeyAndCert, not_before: Date, not_after: Date):
        KeyAndCert {
    return newCert(subject, issuer.cert.fullSubject, issuer.keyPair, false, true, not_before, not_after)
}

fun newCert(subject: String, issuer: String, issuerKeyPair: KeyPair?, ca: Boolean, timestamp: Boolean, not_before:
Date, not_after: Date):
        KeyAndCert {
    // Generate key pair
    var keyPair = KeyPairGenerator.generateRSA(SecureRandom(), 2048)
    var subPubKeyInfo = SubjectPublicKeyInfo.getInstance(keyPair.public.encoded);

    // Generate certificate
    var certGen = X509v3CertificateBuilder(
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
    var signer : ContentSigner
    if (issuerKeyPair != null) {
        // Not self signed
        signer = JcaContentSignerBuilder("SHA1WithRSA").build(issuerKeyPair.private)
    } else {
        // Self signed
        signer = JcaContentSignerBuilder("SHA1WithRSA").build(keyPair.private)
    }
    var cert = certGen.build(signer)

    //Finish
    return KeyAndCert(cert, keyPair.private)
}
