package no.nav.fo.veilarb.dokument.domain;

import lombok.Value;
import no.nav.common.types.identer.AktorId;
import no.nav.common.types.identer.Fnr;

@Value
public class Bruker {
    Fnr fnr;
    AktorId aktorId;
}
