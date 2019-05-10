package no.nav.fo.veilarb.dokument.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class OppfolgingsenhetDto {
    @JsonProperty("nav_kontor")
    String navKontor;
}
