package no.nav.fo.veilarb.dokument.domain;

import lombok.Value;
import lombok.experimental.Accessors;

@Value
@Accessors(fluent = true)
public class DokumentbestillingDto {
    Person bruker;
    Person mottaker;
    MalType malType;
    String veilederEnhet;
    String begrunnelse;
}
