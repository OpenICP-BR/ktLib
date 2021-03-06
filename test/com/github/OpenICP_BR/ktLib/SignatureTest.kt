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
        ICPinit()
        val p12 = KeyAndCert("test/res/pfx/beltrano.p12", "beltrano")
        val sigBuilder = SignatureBuilder()
        sigBuilder.setMsg("Hello World")
        sigBuilder.setSignerLocation("Emerald City", 0)
        val sig = sigBuilder.finish(p12, true)
        sig.save("tmp/test.sig")
        assertTrue(sig.verify())
    }

    @Test
    fun counterSign() {
        ICPinit()

        val oldSig = LoadOneSignature("test/res/hello-world-beltrano.p7s")

        val p12 = KeyAndCert("test/res/pfx/ciclano.p12", "ciclano")
        val sigBuilder = SignatureBuilder()
        sigBuilder.setMsg(oldSig)
        sigBuilder.setSignerLocation("Kansas", 0)
        val sig = sigBuilder.finish(p12, true)
        sig.save("tmp/test.sig")
        assertTrue(sig.verify())
    }
}