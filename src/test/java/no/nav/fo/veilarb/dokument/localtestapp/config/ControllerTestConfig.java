package no.nav.fo.veilarb.dokument.localtestapp.config;

import no.nav.fo.veilarb.dokument.controller.DokumentController;
import no.nav.fo.veilarb.dokument.controller.InternalController;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        DokumentController.class,
        InternalController.class,
})
public class ControllerTestConfig {
}
