package no.nav.fo.veilarb.dokument.domain;

import lombok.Value;
import no.nav.common.types.identer.EnhetId;

@Value
public class EnhetKontaktinformasjon {
    EnhetId enhetNr;
    EnhetPostadresse postadresse;
}
