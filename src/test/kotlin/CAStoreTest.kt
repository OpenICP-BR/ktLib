package com.github.OpenICP_BR.ICP

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.util.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CAStoreTest {
    @Test
    fun testing_ca_invalid() {
        val store = CAStore()
        val cert = Certificate("res/certs/ICP-Brasil.crt")
        assertThrows(IllegalArgumentException::class.java) {
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