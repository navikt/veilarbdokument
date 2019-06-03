package no.nav.fo.veilarb.dokument.domain;

import org.junit.Test;

import java.util.stream.Stream;

import static no.nav.fo.veilarb.dokument.domain.MalType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class MalTypeTest {

    @Test
    public void getMalKode__mapper_til_riktige_malkoder() {
        assertThat(MalType.getMalKode(STANDARD_INNSATS_SKAFFE_ARBEID_PROFILERING)).isEqualTo("000134");
        assertThat(MalType.getMalKode(SITUASJONSBESTEMT_INNSATS_SKAFFE_ARBEID)).isEqualTo("000135");
        assertThat(MalType.getMalKode(STANDARD_INNSATS_SKAFFE_ARBEID)).isEqualTo("000136");
        assertThat(MalType.getMalKode(STANDARD_INNSATS_BEHOLDE_ARBEID)).isEqualTo("000137");
        assertThat(MalType.getMalKode(SITUASJONSBESTEMT_INNSATS_BEHOLDE_ARBEID)).isEqualTo("000138");
        assertThat(MalType.getMalKode(SPESIELT_TILPASSET_INNSATS_SKAFFE_ARBEID)).isEqualTo("000139");
        assertThat(MalType.getMalKode(SPESIELT_TILPASSET_INNSATS_BEHOLDE_ARBEID)).isEqualTo("000140");
        assertThat(MalType.getMalKode(GRADERT_VARIG_TILPASSET_INNSATS)).isEqualTo("000141");
        assertThat(MalType.getMalKode(VARIG_TILPASSET_INNSATS)).isEqualTo("000144");
    }

    @Test
    public void getMalKode__skal_ha_malkode_for_alle_verdier_uten_exception() {
        assertAll(Stream.of(MalType.values()).map(malType -> () -> getMalKode(malType)));
    }
}
