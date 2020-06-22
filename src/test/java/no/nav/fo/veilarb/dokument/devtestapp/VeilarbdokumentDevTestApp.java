package no.nav.fo.veilarb.dokument.devtestapp;

import no.nav.common.utils.Credentials;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Properties;

import static no.nav.common.utils.EnvironmentUtils.getRequiredProperty;

@SpringBootApplication(scanBasePackages = {"no.nav.fo.veilarb.dokument"})
public class VeilarbdokumentDevTestApp {
    public static void main(String... args) {
        TestConfig.setupTestContext();
//        SslUtils.setupTruststore();

        Properties properties = new Properties();
        properties.put("spring.main.allow-bean-definition-overriding", "true");

        SpringApplication application = new SpringApplication(VeilarbdokumentDevTestApp.class);
        application.setDefaultProperties(properties);

        application.run(args);
    }

    @Bean
    public Credentials serviceUserCredentials() {
        return new Credentials(
                getRequiredProperty("SRVVEILARBDOKUMENT_USERNAME"),
                getRequiredProperty("SRVVEILARBDOKUMENT_PASSWORD"));
    }
}
