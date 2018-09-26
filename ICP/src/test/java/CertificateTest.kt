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
import java.text.SimpleDateFormat
import java.util.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CertificateTest {
    @Test
    fun docFormatting() {
        val cert = Certificate()
        cert.parseName("Abc:123")
        assertEquals("123", cert.personId)
        assertEquals("Abc", cert.subjectName)
        cert.parseName("Abc:123:")
        assertEquals("123:", cert.personId)
        assertEquals("Abc", cert.subjectName)
        cert.parseName("  宮本 茂. Shigeru Miyamoto  :  12345678911  ")
        assertEquals("123.456.789-11", cert.personId)
        assertEquals("宮本 茂. Shigeru Miyamoto", cert.subjectName)
        cert.parseName("Nintendo Co., Ltd.:00530352000159")
        assertEquals("00.530.352/0001-59", cert.personId)
        assertEquals("Nintendo Co., Ltd.", cert.subjectName)
    }

    @Test
    fun isSelfSigned() {
        var cert = Certificate()
        assertFalse(cert.isSelfSigned())
    }

    @Test
    fun loadFromFile_1() {
        var cert = Certificate("res/certs/ICP-Brasil.crt")
        assertTrue(cert.isSelfSigned())
        assertEquals("", cert.personId)
        assertEquals("Autoridade Certificadora Raiz Brasileira v1", cert.subjectName)
        assertEquals("Autoridade Certificadora Raiz Brasileira v1", cert.issuerName)
        assertEquals("42:B2:2C:5C:74:01:07:BE:9B:FF:55:33:3B:EE:29:BB:5D:91:BF:06", cert.subjectKeyId)
        assertEquals("", cert.authorityKeyId)
        assertEquals("C=BR, O=ICP-Brasil, OU=Instituto Nacional de Tecnologia da Informacao - ITI, CN=Autoridade " +
                "Certificadora Raiz Brasileira v1", cert.fullIssuer)
        assertEquals("C=BR, O=ICP-Brasil, OU=Instituto Nacional de Tecnologia da Informacao - ITI, CN=Autoridade " +
                "Certificadora Raiz Brasileira v1", cert.fullSubject)

        var formatter = SimpleDateFormat("yyyy-MM-dd H:m:ss")
        formatter.timeZone = TimeZone.getTimeZone("GMT")
        assertEquals("2008-07-29 19:17:10", formatter.format(cert.notBefore))
        assertEquals("2021-07-29 19:17:10", formatter.format(cert.notAfter))
    }

    fun loadFromFile_2() {
        var cert = Certificate("res/certs/AC_OAB_G3.crt")
        assertTrue(cert.isSelfSigned())
        assertEquals("", cert.personId)
        assertEquals("AC OAB G3", cert.subjectName)
        assertEquals("AC Certisign G7", cert.issuerName)
        assertEquals("39:F2:78:9E:DB:D6:B0:9D:05:56:68:33:B4:F0:D0:1F:56:BB:07:0A", cert.subjectKeyId)
        assertEquals("81:82:2F:D8:C6:7D:2D:6E:F8:F9:47:C8:A2:84:D4:19:26:0D:DE:08", cert.authorityKeyId)
        assertEquals("C=BR, O=ICP-Brasil, OU=Autoridade Certificadora Raiz Brasileira v5, CN=AC Certisign G7", cert.fullIssuer)
        assertEquals("C=BR, O=ICP-Brasil, OU=ORDEM DOS ADVOGADOS DO BRASIL CONSELHO FEDERAL, CN=AC OAB G3", cert.fullSubject)

        var formatter = SimpleDateFormat("yyyy-MM-dd H:m:ss")
        formatter.timeZone = TimeZone.getTimeZone("GMT")
        assertEquals("2016-11-19 02:00:00", formatter.format(cert.notBefore))
        assertEquals("2029-03-01 03:00:00", formatter.format(cert.notAfter))
    }

    @Test
    fun loadFromFile_NonExistant() {
        assertThrows(java.io.FileNotFoundException::class.java) {
            Certificate("res/certs/ICP-Brasil.crt2")
        }
    }

    @Test
    fun loadFromFile_NotCert() {
        assertThrows(org.cryptacular.StreamException::class.java) {
            var cert = Certificate("res/certs/not_a_certificate.txt")
            assertNull(cert.base)
        }
    }
}
