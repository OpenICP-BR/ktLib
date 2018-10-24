package com.github.OpenICP_BR.ktLib

import com.github.OpenICP_BR.ktLib.KeyAndCert
import com.github.OpenICP_BR.ktLib.TESTING_ROOT_CA_SUBJECT
import com.github.OpenICP_BR.ktLib.newCert
import com.github.OpenICP_BR.ktLib.newTestRootCA
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.util.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KeyAndCertTest {
    @Test
    fun get_pfx_1() {
        var p12 = KeyAndCert("test/res/pfx/beltrano.p12", "beltrano")
        assertEquals("C=BR, ST=FN, O=FakeBank, CN=FakeBank", p12.cert.fullIssuer)
        assertEquals("C=BR, ST=FN, L=Ilha, O=FakePKI, CN=Beltrano Freitas:41951116844, " +
                "EMAILADDRESS=beltrano@exemple.com", p12.cert.fullSubject)
    }

    @Test
    fun generate_root() {
        var kc = newTestRootCA(Date(), Date())
        assertEquals(TESTING_ROOT_CA_SUBJECT, kc.cert.fullIssuer)
        assertEquals(TESTING_ROOT_CA_SUBJECT, kc.cert.fullSubject)
    }

    @Test
    fun generate_end_cert() {
        var root = newTestRootCA(Date(), Date())
        assertEquals(TESTING_ROOT_CA_SUBJECT, root.cert.fullIssuer)
        assertEquals(TESTING_ROOT_CA_SUBJECT, root.cert.fullSubject)

        var kc = newCert("C=BR, CN=Zé Qualquer",  root, Date(), Date())
        assertEquals(TESTING_ROOT_CA_SUBJECT, kc.cert.fullIssuer)
        assertEquals("C=BR, CN=Zé Qualquer", kc.cert.fullSubject)
    }
}
