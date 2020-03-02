package no.nav.fo.veilarb.dokument.helsesjekk;


import no.nav.apiapp.selftest.Helsesjekk;
import no.nav.apiapp.selftest.HelsesjekkMetadata;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v3.DokumentproduksjonV3;
import org.springframework.stereotype.Component;

import static no.nav.fo.veilarb.dokument.config.ApplicationConfig.getDokumentproduksjonEndpointUrl;

@Component
public class DokumentproduksjonV3Helsesjekk implements Helsesjekk {

    private final DokumentproduksjonV3 dokumentproduksjonV3;

    public DokumentproduksjonV3Helsesjekk(DokumentproduksjonV3 dokumentproduksjonV3) {
        this.dokumentproduksjonV3 = dokumentproduksjonV3;
    }

    @Override
    public void helsesjekk() throws Throwable {
        dokumentproduksjonV3.ping();
    }

    @Override
    public HelsesjekkMetadata getMetadata() {
        return new HelsesjekkMetadata(
                "DokumentproduksjonV3 helsesjekk",
                getDokumentproduksjonEndpointUrl(),
                "DokumentproduksjonV3 - ping",
                true
        );
    }
}
