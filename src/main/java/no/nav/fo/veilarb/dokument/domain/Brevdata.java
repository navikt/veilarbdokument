package no.nav.fo.veilarb.dokument.domain;

import lombok.Builder;
import lombok.Value;
import lombok.experimental.Accessors;

import java.util.List;

@Value
@Accessors(fluent = true)
@Builder
public class Brevdata {
    String brukerFnr;
    MalType malType;
    String enhet;
    String veilederId;
    String veilederNavn;
    String begrunnelse;
    List<String> kilder;
}
