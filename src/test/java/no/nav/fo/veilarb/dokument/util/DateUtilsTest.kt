package no.nav.fo.veilarb.dokument.util

import no.nav.fo.veilarb.dokument.util.DateUtils.norskDateFormatter
import org.junit.Test
import org.junit.Assert.assertEquals
import java.time.LocalDate.of

class DateUtilsTest {

    @Test
    fun formaterer_dato_riktig_for_alle_mnd() {
        assertEquals("5. januar 2020", of(2020, 1, 5).format(norskDateFormatter))
        assertEquals("1. februar 2021", of(2021, 2, 1).format(norskDateFormatter))
        assertEquals("9. mars 2022", of(2022, 3, 9).format(norskDateFormatter))
        assertEquals("17. april 2023", of(2023, 4, 17).format(norskDateFormatter))
        assertEquals("23. mai 2016", of(2016, 5, 23).format(norskDateFormatter))
        assertEquals("4. juni 2017", of(2017, 6, 4).format(norskDateFormatter))
        assertEquals("19. juli 2018", of(2018, 7, 19).format(norskDateFormatter))
        assertEquals("28. august 2019", of(2019, 8, 28).format(norskDateFormatter))
        assertEquals("11. september 2024", of(2024, 9, 11).format(norskDateFormatter))
        assertEquals("31. oktober 2025", of(2025, 10, 31).format(norskDateFormatter))
        assertEquals("27. november 2026", of(2026, 11, 27).format(norskDateFormatter))
        assertEquals("15. desember 2027", of(2027, 12, 15).format(norskDateFormatter))
    }
}
