package com.github.OpenICP_BR.ktLib

import org.bouncycastle.asn1.DERBMPString
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers
import org.bouncycastle.cert.X509CertificateHolder
import org.bouncycastle.pkcs.PKCS12PfxPduBuilder
import java.io.FileInputStream
import java.security.KeyStore
import java.security.PrivateKey
import java.security.cert.X509Certificate
import java.security.KeyPair
import org.bouncycastle.pkcs.jcajce.JcaPKCS12SafeBagBuilder
import org.bouncycastle.pkcs.PKCS12SafeBagBuilder
import org.bouncycastle.crypto.engines.DESedeEngine
import org.bouncycastle.crypto.modes.CBCBlockCipher
import org.bouncycastle.pkcs.bc.BcPKCS12PBEOutputEncryptorBuilder
import org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils
import org.bouncycastle.pkcs.bc.BcPKCS12MacCalculatorBuilder
import org.bouncycastle.pkcs.PKCS12PfxPdu
import org.bouncycastle.crypto.engines.RC2Engine
import org.bouncycastle.pkcs.PKCS12SafeBag
import java.io.File


/**
 * Represents a .pfx or .p12 file. This is, a certificate and the key pair.
 */
class KeyAndCert {
    lateinit var cert : Certificate
        internal set
    var privateKey : PrivateKey? = null
    var keyPair : KeyPair? = null
        get() = KeyPair(this.cert.base!!.publicKey, this.privateKey)

    constructor(new_cert: X509CertificateHolder, new_key: PrivateKey) {
        val bytes = new_cert.toASN1Structure().toASN1Primitive().encoded
        this.cert = Certificate(bytes.inputStream())
        this.privateKey = new_key
    }

    constructor(new_cert: Certificate, new_key: PrivateKey) {
        this.cert = new_cert
        this.privateKey = new_key
    }

    fun hasKey(): Boolean {
        return this.privateKey != null
    }

    fun save(path: String, password: String) {
        save(path, password.toCharArray())
    }

    fun save(path: String, password: CharArray) {
        val extUtils = JcaX509ExtensionUtils()

        // Bag the certificate and add a friendly name to it
        val taCertBagBuilder = JcaPKCS12SafeBagBuilder(this.cert.base)
        taCertBagBuilder.addBagAttribute(
                PKCSObjectIdentifiers.pkcs_9_at_friendlyName,
                DERBMPString(this.cert.subjectName))

        // Bag the private keys and add a friendly name to it
        val keyBagBuilder = JcaPKCS12SafeBagBuilder(
                this.privateKey,
                BcPKCS12PBEOutputEncryptorBuilder(
                        PKCSObjectIdentifiers.pbeWithSHAAnd3_KeyTripleDES_CBC,
                        CBCBlockCipher(DESedeEngine())).build(password))
        keyBagBuilder.addBagAttribute(
                PKCSObjectIdentifiers.pkcs_9_at_friendlyName,
                DERBMPString(this.cert.subjectName))
        keyBagBuilder.addBagAttribute(
                PKCSObjectIdentifiers.pkcs_9_at_localKeyId,
                extUtils.createSubjectKeyIdentifier(this.cert.base!!.publicKey))

        val pfxPduBuilder = PKCS12PfxPduBuilder()

        val certs = arrayOfNulls<PKCS12SafeBag>(1)
        certs[0] = taCertBagBuilder.build()

        pfxPduBuilder.addEncryptedData(
                BcPKCS12PBEOutputEncryptorBuilder(
                        PKCSObjectIdentifiers.pbeWithSHAAnd40BitRC2_CBC, CBCBlockCipher(RC2Engine())
                ).build(password), certs)

        pfxPduBuilder.addData(keyBagBuilder.build())

        val pfx = pfxPduBuilder.build(BcPKCS12MacCalculatorBuilder(), password)
        File(path).writeBytes(pfx.encoded)
    }

    constructor(path: String, password: String) : this(path, password, password) {
    }

    constructor(path: String, password: String, password_key: String) {
        val p12 = KeyStore.getInstance("pkcs12")
        p12.load(FileInputStream(path), password.toCharArray())
        val e = p12.aliases()
        var got = false
        while (e.hasMoreElements()) {
            val alias = e.nextElement() as String
            val c = Certificate(p12.getCertificate(alias) as X509Certificate)
            this.cert = c
            val k = p12.getKey(alias, password_key.toCharArray())
            this.privateKey = k as PrivateKey
            got = true
            // We only need one certificate and key
            break
        }

        if (!got) {
            throw Exception("failed to find certificate and private key")
        }
    }
}
