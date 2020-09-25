package no.nav.fo.veilarb.dokument.domain;

import lombok.Value;
import lombok.experimental.Accessors;
import no.nav.common.types.identer.AktorId;

@Value
@Accessors(fluent = true)
public class OpprettSakDto {
    String tema;
    String applikasjon;
    AktorId aktoerId;
    String fagsakNr;
}
