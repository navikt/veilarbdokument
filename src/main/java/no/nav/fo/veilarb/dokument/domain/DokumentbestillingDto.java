package no.nav.fo.veilarb.dokument.domain;

import lombok.Value;
import lombok.experimental.Accessors;
import no.nav.common.types.identer.EnhetId;
import no.nav.common.types.identer.Fnr;

import java.util.List;

@Value
@Accessors(fluent = true)
public class DokumentbestillingDto {
    Fnr brukerFnr;
    MalType malType;
    EnhetId enhetId;
    String begrunnelse;
    List<String> opplysninger;
}
