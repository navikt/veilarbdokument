package no.nav.fo.veilarb.dokument.domain;

import lombok.Value;

@Value(staticConstructor = "of")
public class DokumentbestillingResponsDto {
    String journalpostId;
    String dokumentId;
}
