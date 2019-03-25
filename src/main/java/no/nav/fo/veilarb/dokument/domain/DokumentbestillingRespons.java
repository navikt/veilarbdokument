package no.nav.fo.veilarb.dokument.domain;

import lombok.Value;

@Value(staticConstructor = "of")
public class DokumentbestillingRespons {
    String journalpostId;
    String dokumentId;
}
