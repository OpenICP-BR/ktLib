package ICP.openicpbr.github.com
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

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RootCAsTest {
    @Test
    fun getRootCert_V1() {
        val cert = getRootCert("V1")
        assertEquals("C=BR, O=ICP-Brasil, OU=Instituto Nacional de Tecnologia da Informacao - ITI, CN=Autoridade " +
                "Certificadora Raiz Brasileira v1", cert.fullSubject)
        assertEquals("C=BR, O=ICP-Brasil, OU=Instituto Nacional de Tecnologia da Informacao - ITI, CN=Autoridade " +
                "Certificadora Raiz Brasileira v1", cert.fullIssuer)
        assertTrue(cert.isCA())
        assertTrue(cert.isSelfSigned())
    }

    @Test
    fun getRootCert_V2() {
        val cert = getRootCert("V2")
        assertEquals("C=BR, O=ICP-Brasil, OU=Instituto Nacional de Tecnologia da Informacao - ITI, CN=Autoridade " +
                "Certificadora Raiz Brasileira v2", cert.fullSubject)
        assertEquals("C=BR, O=ICP-Brasil, OU=Instituto Nacional de Tecnologia da Informacao - ITI, CN=Autoridade " +
                "Certificadora Raiz Brasileira v2", cert.fullIssuer)
        assertTrue(cert.isCA())
        assertTrue(cert.isSelfSigned())
    }

    @Test
    fun getRootCert_V5() {
        val cert = getRootCert("V5")
        assertEquals("C=BR, O=ICP-Brasil, OU=Instituto Nacional de Tecnologia da Informacao - ITI, CN=Autoridade " +
                "Certificadora Raiz Brasileira v5", cert.fullSubject)
        assertEquals("C=BR, O=ICP-Brasil, OU=Instituto Nacional de Tecnologia da Informacao - ITI, CN=Autoridade " +
                "Certificadora Raiz Brasileira v5", cert.fullIssuer)
        assertTrue(cert.isCA())
        assertTrue(cert.isSelfSigned())
    }

    @Test
    fun getRootCert_other() {
        assertThrows(java.lang.IllegalArgumentException::class.java) {
            getRootCert("other")
        }
    }

    @Test
    fun verifyCert_1() {
        var store = CAStore()
        var cert_final = Certificate("res/certs/AC_OAB_G3.crt")
        var cert_intermediate = Certificate("res/certs/AC_Certisign_G7.crt")

        assertThrows(NoSuchElementException::class.java) {
            store.verifyCert(cert_final)
        }

        assertFalse(store.verifyCertBool(cert_final))
        assertTrue(store.verifyCertBool(cert_intermediate))
        assertTrue(store.addCA(cert_intermediate))
        assertTrue(store.verifyCertBool(cert_final))
    }
}
