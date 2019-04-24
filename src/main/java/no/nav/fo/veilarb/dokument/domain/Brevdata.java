package no.nav.fo.veilarb.dokument.domain;

import lombok.Builder;
import lombok.Value;
import lombok.experimental.Accessors;

@Value
@Accessors(fluent = true)
@Builder
public class Brevdata {
    Person bruker;
    Person mottaker;
    MalType malType;
    String veilederEnhet;
    String veilederId;
    String begrunnelse;
}
