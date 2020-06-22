package no.nav.fo.veilarb.dokument.localtestapp;

import no.nav.fo.veilarb.dokument.localtestapp.config.ApplicationTestConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Import;

import static no.nav.common.utils.EnvironmentUtils.NAIS_APP_NAME_PROPERTY_NAME;
import static no.nav.fo.veilarb.dokument.config.ApplicationConfig.APPLICATION_NAME;

@EnableAutoConfiguration
@Import(ApplicationTestConfig.class)
public class VeilarbdokumentTestApp {

    public static void main(String[] args) {

        System.setProperty(NAIS_APP_NAME_PROPERTY_NAME, APPLICATION_NAME);

        SpringApplication application = new SpringApplication(VeilarbdokumentTestApp.class);
        application.setAdditionalProfiles("local");
        application.run(args);
    }

}
