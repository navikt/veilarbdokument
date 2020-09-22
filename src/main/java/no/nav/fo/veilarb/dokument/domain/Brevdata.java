package no.nav.fo.veilarb.dokument.domain;

import lombok.Builder;
import lombok.Value;
import lombok.experimental.Accessors;
import no.nav.common.types.identer.EnhetId;
import no.nav.common.types.identer.Fnr;
import no.nav.common.types.identer.NavIdent;

import java.util.List;

@Value
@Accessors(fluent = true)
@Builder
public class Brevdata {
    Fnr brukerFnr;
    MalType malType;
    EnhetId enhetId;
    EnhetId enhetIdKontakt;
    NavIdent veilederId;
    String veilederNavn;
    String begrunnelse;
    List<String> kilder;
}
