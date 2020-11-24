package no.nav.fo.veilarb.dokument.localtestapp.config;

import no.nav.fo.veilarb.dokument.service.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        AuthService.class,
        DokumentService.class,
        DokumentV2Service.class,
        EnhetInfoService.class,
        MetrikkService.class,
        OppfolgingssakService.class
})
public class ServiceTestConfig {
}
