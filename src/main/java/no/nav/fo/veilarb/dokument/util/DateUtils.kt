package no.nav.fo.veilarb.dokument.util

import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.format.FormatStyle
import java.util.*

object DateUtils {
    val norskDateFormatter: DateTimeFormatter = DateTimeFormatterBuilder()
            .appendLocalized(FormatStyle.LONG, null)
            .toFormatter(Locale("no", "NO"))
}
