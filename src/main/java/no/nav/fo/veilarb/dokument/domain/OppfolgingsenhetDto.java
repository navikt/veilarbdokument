package no.nav.fo.veilarb.dokument.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;
import no.nav.common.types.identer.EnhetId;

@Value
public class OppfolgingsenhetDto {
    @JsonProperty("nav_kontor")
    EnhetId navKontor;
}
