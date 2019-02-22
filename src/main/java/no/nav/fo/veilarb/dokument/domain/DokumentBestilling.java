package no.nav.fo.veilarb.dokument.domain;

import lombok.Value;
import lombok.experimental.Accessors;

@Value
@Accessors(fluent = true)
public class DokumentBestilling {
    String fnr;
    String dokumentTypeId;
}
