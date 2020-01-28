package no.nav.fo.veilarb.dokument.domain;

import lombok.Value;
import lombok.experimental.Accessors;

import java.util.List;

@Value
@Accessors(fluent = true)
public class DokumentbestillingDto {
    String brukerFnr;
    MalType malType;
    String veilederEnhet;
    String begrunnelse;
    List<String> opplysninger;
}
