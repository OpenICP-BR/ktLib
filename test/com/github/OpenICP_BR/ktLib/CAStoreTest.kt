package com.github.OpenICP_BR.ktLib

import com.github.OpenICP_BR.ktLib.CAStore
import com.github.OpenICP_BR.ktLib.Certificate
import com.github.OpenICP_BR.ktLib.newTestRootCA
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CAStoreTest {
    @Test
    fun testing_ca_invalid() {
        val store = CAStore()
        val cert = Certificate("test/res/certs/ICP-Brasil.crt")
        assertThrows(IllegalTestingRootCA::class.java) {
            store.addTestingCA(cert)
        }
    }

    @Test
    fun testing_ca_valid() {
        val store = CAStore()
        val pfx = newTestRootCA()
        assertTrue(store.addTestingCA(pfx.cert))
    }
}