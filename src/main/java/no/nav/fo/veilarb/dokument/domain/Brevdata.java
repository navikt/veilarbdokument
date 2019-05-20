package no.nav.fo.veilarb.dokument.domain;

import lombok.Builder;
import lombok.Value;
import lombok.experimental.Accessors;

import java.util.List;

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
    List<String> kilder;
}
