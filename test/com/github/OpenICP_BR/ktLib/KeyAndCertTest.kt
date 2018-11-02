package com.github.OpenICP_BR.ktLib

import com.github.OpenICP_BR.ktLib.KeyAndCert
import com.github.OpenICP_BR.ktLib.TESTING_ROOT_CA_SUBJECT
import com.github.OpenICP_BR.ktLib.newTestRootCA
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.io.File
import java.util.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KeyAndCertTest {
    fun build_all_certs() {
        var builder = KeyAndCertBuilder()
        builder.isRoot = true
        builder.isCA = true
        val root = builder.build()
        root.save("test/res/pfx/root.p12", "root")

        builder = KeyAndCertBuilder()
        builder.issuer = root
        builder.subjectCN = "Ciclano Gonçalves Müler:00000000000"
        builder.subjectEmail = "c.gonçalves@example.com"
        builder.build().save("test/res/pfx/ciclano.p12", "ciclano")
        builder.subjectCN = "Beltrano Freitas:00000000000"
        builder.subjectEmail = "beltrano@example.com"
        builder.build().save("test/res/pfx/beltrano.p12", "beltrano")
        builder.subjectCN = "Fulano Silva:00000000000"
        builder.subjectEmail = "fulano@example.com"
        builder.build().save("test/res/pfx/fulano.p12", "fulano")
    }

    @Test
    fun build_certs() {
        var builder = KeyAndCertBuilder()
        builder.isRoot = true
        builder.isCA = true
        val root = builder.build()
        root.save("test/res/pfx/root2.p12", "root")

        builder = KeyAndCertBuilder()
        builder.issuer = root
        builder.subjectCN = "Ciclano Gonçalves Müler:00000000000"
        builder.subjectEmail = "c.gonçalves@example.com"
        builder.build().save("test/res/pfx/ciclano2.p12", "ciclano")

        var file: File
        file = File("test/res/pfx/root2.p12")
        if (file.exists()) file.delete()
        file = File("test/res/pfx/ciclano2.p12")
        if (file.exists()) file.delete()
    }

    @Test
    fun get_pfx_1() {
        val p12 = KeyAndCert("test/res/pfx/beltrano.p12", "beltrano")
        assertEquals("C=BR, O=FakeICP - SEM VALOR LEGAL, CN=Fake AC Raíz", p12.cert.fullIssuer)
        assertEquals("C=BR, O=FakeICP - SEM VALOR LEGAL, CN=Beltrano Freitas:00000000000, EMAILADDRESS=beltrano@example.com", p12.cert.fullSubject)
    }

    @Test
    fun generate_root() {
        val kc = newTestRootCA()
        assertEquals(TESTING_ROOT_CA_SUBJECT, kc.cert.fullIssuer)
        assertEquals(TESTING_ROOT_CA_SUBJECT, kc.cert.fullSubject)
    }

    @Test
    fun generate_end_cert() {
        val root = newTestRootCA()
        assertEquals(TESTING_ROOT_CA_SUBJECT, root.cert.fullIssuer)
        assertEquals(TESTING_ROOT_CA_SUBJECT, root.cert.fullSubject)

        val kc = newCert("Zé Qualquer",  root)
        assertEquals(TESTING_ROOT_CA_SUBJECT, kc.cert.fullIssuer)
        assertEquals("C=BR, O=FakeICP - SEM VALOR LEGAL, CN=Zé Qualquer", kc.cert.fullSubject)
    }
}
