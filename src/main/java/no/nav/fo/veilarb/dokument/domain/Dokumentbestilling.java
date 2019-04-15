package no.nav.fo.veilarb.dokument.domain;

import lombok.Value;
import lombok.experimental.Accessors;

@Value
@Accessors(fluent = true)
public class Dokumentbestilling {
    Person bruker;
    Person mottaker;
    MalType malType;
    String veilederEnhet;
    Adresse adresse;
    String begrunnelse;
}
