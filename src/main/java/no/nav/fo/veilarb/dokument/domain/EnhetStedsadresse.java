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
}
