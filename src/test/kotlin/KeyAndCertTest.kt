package com.github.openicpbr.ICP
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

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.util.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KeyAndCertTest {
    @Test
    fun get_pfx_1() {
        var p12 = KeyAndCert("res/pfx/beltrano.p12", "beltrano")
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
