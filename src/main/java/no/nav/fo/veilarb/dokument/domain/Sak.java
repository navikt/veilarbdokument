package no.nav.fo.veilarb.dokument.domain;

import lombok.Value;
import lombok.experimental.Accessors;

import java.time.ZonedDateTime;

@Value
@Accessors(fluent = true)
public class Sak {
    int id;
    String tema;
    String applikasjon;
    String fagsakNr;
    ZonedDateTime opprettetTidspunkt;
}
