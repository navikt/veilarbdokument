package no.nav.fo.veilarb.dokument.localtestapp.config;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import no.nav.common.abac.AbacClient;
import no.nav.common.abac.Pep;
import no.nav.common.abac.VeilarbPep;
import no.nav.common.abac.audit.AuditLogger;
import no.nav.common.client.aktorregister.AktorregisterClient;
import no.nav.common.cxf.CXFClient;
import no.nav.common.featuretoggle.UnleashService;
import no.nav.common.utils.Credentials;
import no.nav.fo.veilarb.dokument.config.ReadinessDependenciesHealthIndicator;
import no.nav.fo.veilarb.dokument.config.SwaggerConfig;
import no.nav.fo.veilarb.dokument.helsesjekk.DokumentproduksjonV3Helsesjekk;
import no.nav.fo.veilarb.dokument.localtestapp.stub.AbacClientStub;
import no.nav.fo.veilarb.dokument.localtestapp.stub.AktorregisterClientStub;
import no.nav.fo.veilarb.dokument.localtestapp.stub.UnleashServiceMock;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v3.DokumentproduksjonV3;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

import static no.nav.fo.veilarb.dokument.localtestapp.stub.Values.TEST_SRV_PASSWORD;
import static no.nav.fo.veilarb.dokument.localtestapp.stub.Values.TEST_SRV_USERNAME;

@Configuration
@Profile("local")
@Import({
        ReadinessDependenciesHealthIndicator.class,
        SwaggerConfig.class,
        ClientTestConfig.class,
        ServiceTestConfig.class,
        ControllerTestConfig.class,
        FilterTestConfig.class,
        DokumentproduksjonV3Helsesjekk.class
})
public class ApplicationTestConfig {

    @Bean
    public Credentials serviceUserCredentials() {
        return new Credentials(TEST_SRV_USERNAME, TEST_SRV_PASSWORD);
    }

    @Bean
    public AktorregisterClient aktorregisterClient() {
        return new AktorregisterClientStub();
    }

    @Bean
    public AbacClient abacClient() {
        return new AbacClientStub();
    }

    @Bean
    public Pep veilarbPep(AbacClient abacClient) {
        return new VeilarbPep(TEST_SRV_USERNAME, abacClient, new AuditLogger(), null, null);
    }

    @Bean
    public MeterRegistry metricsClient() {
        return new SimpleMeterRegistry();
    }

    @Bean
    public DokumentproduksjonV3 dokumentproduksjonV3() {
        return new CXFClient<>(DokumentproduksjonV3.class)
                .address("http://localhost:8080/veilarbdokument/stub/dokument")
                .build();
    }

    @Bean
    public UnleashService unleashService() {
        return UnleashServiceMock.getMock();
    }
}
