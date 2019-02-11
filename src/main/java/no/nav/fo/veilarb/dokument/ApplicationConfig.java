package no.nav.fo.veilarb.dokument;

import no.nav.apiapp.ApiApplication;
import no.nav.apiapp.config.ApiAppConfigurator;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        DokumentRessurs.class
})
public class ApplicationConfig implements ApiApplication {

    @Override
    public void configure(ApiAppConfigurator apiAppConfigurator) {

    }
}
