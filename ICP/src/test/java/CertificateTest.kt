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
        assertEquals("123", cert.id)
        assertEquals("Abc", cert.name)
        cert.parseName("Abc:123:")
        assertEquals("123:", cert.id)
        assertEquals("Abc", cert.name)
        cert.parseName("  宮本 茂. Shigeru Miyamoto  :  12345678911  ")
        assertEquals("123.456.789-11", cert.id)
        assertEquals("宮本 茂. Shigeru Miyamoto", cert.name)
        cert.parseName("Nintendo Co., Ltd.:00530352000159")
        assertEquals("00.530.352/0001-59", cert.id)
        assertEquals("Nintendo Co., Ltd.", cert.name)
    }

    @Test
    fun isSelfSigned() {
        var cert = Certificate()
        assertFalse(cert.isSelfSigned())
    }

    @Test
    fun loadFromFile() {
        var cert = Certificate("res/certs/ICP-Brasil.crt")
        assertTrue(cert.isSelfSigned())
        assertEquals("", cert.id)
        assertEquals("Autoridade Certificadora Raiz Brasileira v1", cert.name)

        var formatter = SimpleDateFormat("yyyy-MM-dd H:m:ss")
        formatter.timeZone = TimeZone.getTimeZone("GMT")
        assertEquals("2008-07-29 19:17:10", formatter.format(cert.notBefore))
        assertEquals("2021-07-29 19:17:10", formatter.format(cert.notAfter))
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
