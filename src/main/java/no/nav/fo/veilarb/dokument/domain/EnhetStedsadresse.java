package no.nav.fo.veilarb.dokument.domain;

import lombok.Value;

@Value
public class EnhetStedsadresse extends EnhetPostadresse {
    String postnummer;
    String poststed;
    String gatenavn;
    String husnummer;
    String husbokstav;
    String adresseTilleggsnavn;

    public String getPostnummer() {
        return this.postnummer;
    }

    public String getPoststed() {
        return this.poststed;
    }

    public String getGatenavn() {
        return this.gatenavn;
    }

    public String getHusnummer() {
        return this.husnummer;
    }

    public String getHusbokstav() {
        return this.husbokstav;
    }

    public String getAdresseTilleggsnavn() {
        return this.adresseTilleggsnavn;
    }
}
