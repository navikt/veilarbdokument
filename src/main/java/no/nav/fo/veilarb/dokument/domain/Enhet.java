package no.nav.fo.veilarb.dokument.domain;

import lombok.Value;
import no.nav.common.types.identer.EnhetId;

@Value
public class Enhet {
    EnhetId nr;
    String navn;
}
