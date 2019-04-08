package no.nav.fo.veilarb.dokument.service;

import no.nav.fo.veilarb.dokument.domain.Sak;
import org.junit.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SakServiceTest {

    private static ZonedDateTime NYESTE_DATO = ZonedDateTime.of(2018,4,12,16,32,22,234, ZoneId.systemDefault());
    private static ZonedDateTime ELDRE_DATO = ZonedDateTime.of(2018,3,28,18,42,12,456, ZoneId.systemDefault());
    private static ZonedDateTime ELDSTE_DATO = ZonedDateTime.of(2017,9,6,10,2,57,687, ZoneId.systemDefault());
    @Test
    public void finnGjeldendeOppfolgingssak__henter_siste_oppfolgingssak() {
        List<Sak> saker = Arrays.asList(
                new Sak(7, "OPP", "AO01", null, NYESTE_DATO),
                new Sak(5, "OPP", "AO01", "5", NYESTE_DATO),
                new Sak(1, "OPP", "AO01", "1", ELDSTE_DATO),
                new Sak(4, "OPP", "FS22", "4", NYESTE_DATO),
                new Sak(3, "OPP", null, null, NYESTE_DATO),
                new Sak(6, null, null, null, NYESTE_DATO),
                new Sak(2, "OPP", "AO01", "2", ELDRE_DATO)
        );

        assertThat(SakService.finnGjeldendeOppfolgingssak(saker).id()).isEqualTo(5);
    }

    @Test(expected=IllegalStateException.class)
    public void finnGjeldendeOppfolgingssak__feiler_ved_kall_med_saker_uten_oppfolgingssak() {
        List<Sak> saker = Collections.singletonList(
                new Sak(4, "OPP", "FS22", "4", NYESTE_DATO)
        );

        SakService.finnGjeldendeOppfolgingssak(saker).id();
    }

    @Test(expected=IllegalStateException.class)
    public void finnGjeldendeOppfolgingssak__feiler_ved_kall_uten_saker() {
        List<Sak> saker = Collections.emptyList();

        SakService.finnGjeldendeOppfolgingssak(saker).id();
    }
}