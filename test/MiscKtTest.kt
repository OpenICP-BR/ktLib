package test

import com.github.OpenICP_BR.ktLib.ICPVersion
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import kotlin.test.assertTrue

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MiscKtTest {
    @Test
    fun get_version() {
        val ver = ICPVersion
        val re = Regex("[A-Z0-9a-z-.]*")
        assertTrue(re.matches(ver))
    }
}