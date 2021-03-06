package no.nav.fo.veilarb.dokument.domain;

import lombok.Builder;
import lombok.Value;
import lombok.experimental.Accessors;

@Value
@Accessors(fluent = true)
@Builder
public class Dokumentbestilling {
    Brevdata brevdata;
    Sak sak;
    String veilederNavn;
}
