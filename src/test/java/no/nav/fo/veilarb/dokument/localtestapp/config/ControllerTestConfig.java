package no.nav.fo.veilarb.dokument.localtestapp.config;

import no.nav.fo.veilarb.dokument.controller.DokumentController;
import no.nav.fo.veilarb.dokument.controller.DokumentV2Controller;
import no.nav.fo.veilarb.dokument.controller.InternalController;
import no.nav.fo.veilarb.dokument.localtestapp.controller.DokumentStubController;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        DokumentController.class,
        DokumentV2Controller.class,
        InternalController.class,
        DokumentStubController.class
})
public class ControllerTestConfig {
}
