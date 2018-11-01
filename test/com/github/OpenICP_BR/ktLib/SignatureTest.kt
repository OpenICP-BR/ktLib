package com.github.OpenICP_BR.ktLib

import com.github.OpenICP_BR.ktLib.CAStore
import com.github.OpenICP_BR.ktLib.Certificate
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.io.FileWriter
import org.bouncycastle.openssl.jcajce.JcaPEMWriter
import java.io.FileOutputStream


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SignatureTest {
    @BeforeAll
    fun prep() {
        ICPinit()
    }

    @Test
    fun signString() {
        val p12 = KeyAndCert("test/res/pfx/beltrano.p12", "beltrano")
        val sigBuilder = SignatureBuilder()
        sigBuilder.setMsg("Hello World")
        sigBuilder.setSignerLocation("Emerald City", 0)
        val sig = sigBuilder.finish(p12, true)
        assertTrue(sig.verify())
    }
}