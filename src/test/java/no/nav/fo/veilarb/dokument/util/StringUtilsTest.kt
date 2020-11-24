package no.nav.fo.veilarb.dokument.util

import org.junit.Assert.assertEquals
import org.junit.Test

class StringUtilsTest {
    @Test
    fun splitter_ved_newline() {
        val splitNewline = StringUtils.splitNewline("a\nb\n\nc\\nd")
        assertEquals(listOf("a", "b", "", "c\\nd"), splitNewline)
    }

}
