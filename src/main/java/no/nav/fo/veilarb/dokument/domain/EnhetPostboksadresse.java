package no.nav.fo.veilarb.dokument.domain;

import lombok.Value;

@Value
public class EnhetPostboksadresse extends EnhetPostadresse {
    String postnummer;
    String poststed;
    String postboksnummer;
    String postboksanlegg;

    public String getPostnummer() {
        return this.postnummer;
    }

    public String getPoststed() {
        return this.poststed;
    }

    public String getPostboksnummer() {
        return this.postboksnummer;
    }

    public String getPostboksanlegg() {
        return this.postboksanlegg;
    }
}
