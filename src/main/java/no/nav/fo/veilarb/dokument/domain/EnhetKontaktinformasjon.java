package no.nav.fo.veilarb.dokument.domain;

import lombok.Value;
import no.nav.common.types.identer.EnhetId;

@Value
public class EnhetKontaktinformasjon {
    private final EnhetId enhetNr;
    private final EnhetPostadresse postadresse;
    private final String telefonnummer;

    public EnhetId getEnhetNr() {
        return this.enhetNr;
    }

    public EnhetPostadresse getPostadresse() {
        return this.postadresse;
    }

    public String getTelefonnummer() {
        return this.telefonnummer;
    }
}
