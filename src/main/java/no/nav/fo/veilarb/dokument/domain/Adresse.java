package no.nav.fo.veilarb.dokument.domain;


import lombok.Value;
import lombok.experimental.Accessors;

@Value
@Accessors(fluent = true)
public class Adresse {
    String adresselinje1;
    String adresselinje2;
    String adresselinje3;
    String land;
    String postnummer;
    String poststed;
}
