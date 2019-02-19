package no.nav.fo.veilarb.dokument;

import no.nav.apiapp.ApiApplication;
import no.nav.apiapp.config.ApiAppConfigurator;
import no.nav.dialogarena.aktor.AktorConfig;
import no.nav.fo.veilarb.dokument.service.DokumentService;
import no.nav.sbl.dialogarena.common.cxf.CXFClient;
import no.nav.sbl.util.EnvironmentUtils;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v3.DokumentproduksjonV3;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        DokumentService.class,
        DokumentRessurs.class,
        AktorConfig.class
})
public class ApplicationConfig implements ApiApplication {

    public static final String DOKUMENTPRODUKSJON_ENDPOINT_URL = "DOKUMENTPRODUKSJON_V3_ENDPOINT_URL";

    @Override
    public void configure(ApiAppConfigurator apiAppConfigurator) {
        apiAppConfigurator
                .addPublicPath("/*");
    }


    @Bean
    public DokumentproduksjonV3 dokumentproduksjonV3() {
        return new CXFClient<>(DokumentproduksjonV3.class)
                .address(getDokumentproduksjonEndpointUrl())
                .configureStsForSystemUser()
                .build();
    }

    static String getDokumentproduksjonEndpointUrl() {
        return EnvironmentUtils.getRequiredProperty(DOKUMENTPRODUKSJON_ENDPOINT_URL);
    }

}
