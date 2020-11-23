package no.nav.fo.veilarb.dokument.util

object StringUtils {
    fun splitNewline(s: String): List<String> {
        return s.split("\n")
    }
}
