package com.github.OpenICP_BR.ktLib

import com.github.OpenICP_BR.ktLib.KeyAndCert
import com.github.OpenICP_BR.ktLib.TESTING_ROOT_CA_SUBJECT
import com.github.OpenICP_BR.ktLib.newTestRootCA
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.util.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KeyAndCertTest {
    @Test
    fun hack() {
        var builder = KeyAndCertBuilder()
        builder.isRoot = true
        builder.isCA = true
        val root = builder.build()
        root.save("test/res/pfx/root.p12", "root")

        builder = KeyAndCertBuilder()
        builder.issuer = root
        builder.subjectCN = "Beltrano Freitas:00000000000"
        builder.build().save("test/res/pfx/beltrano.p12", "beltrano")
        builder.subjectCN = "Fulano Silva:00000000000"
        builder.build().save("test/res/pfx/fulano.p12", "fulano")
        builder.subjectCN = "Ciclano Gonçalves Müler:00000000000"
        builder.build().save("test/res/pfx/ciclano.p12", "ciclano")
    }

    @Test
    fun get_pfx_1() {
        var p12 = KeyAndCert("test/res/pfx/beltrano.p12", "beltrano")
        assertEquals("C=BR, ST=FN, O=FakeBank, CN=FakeBank", p12.cert.fullIssuer)
        assertEquals("C=BR, ST=FN, L=Ilha, O=FakePKI, CN=Beltrano Freitas:41951116844, " +
                "EMAILADDRESS=beltrano@exemple.com", p12.cert.fullSubject)
    }

    @Test
    fun generate_root() {
        var kc = newTestRootCA()
        assertEquals(TESTING_ROOT_CA_SUBJECT, kc.cert.fullIssuer)
        assertEquals(TESTING_ROOT_CA_SUBJECT, kc.cert.fullSubject)
    }

    @Test
    fun generate_end_cert() {
        var root = newTestRootCA()
        assertEquals(TESTING_ROOT_CA_SUBJECT, root.cert.fullIssuer)
        assertEquals(TESTING_ROOT_CA_SUBJECT, root.cert.fullSubject)

        var kc = newCert("C=BR, CN=Zé Qualquer",  root)
        assertEquals(TESTING_ROOT_CA_SUBJECT, kc.cert.fullIssuer)
        assertEquals("C=BR, CN=Zé Qualquer", kc.cert.fullSubject)
    }
}
