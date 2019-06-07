package no.nav.fo.veilarb.dokument.domain;

import lombok.Value;
import lombok.experimental.Accessors;

import java.util.List;

@Value
@Accessors(fluent = true)
public class DokumentbestillingDto {
    Person bruker;
    Person mottaker;
    MalType malType;
    String veilederEnhet;
    String begrunnelse;
    List<String> opplysninger;
}
