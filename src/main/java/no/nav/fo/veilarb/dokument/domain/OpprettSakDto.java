package no.nav.fo.veilarb.dokument.domain;

import lombok.Value;
import lombok.experimental.Accessors;

@Value
@Accessors(fluent = true)
public class OpprettSakDto {
    String tema;
    String applikasjon;
    String aktoerId;
    String fagsakNr;
}
