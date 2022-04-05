package no.nav.fo.veilarb.dokument.localtestapp.config;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import no.nav.common.abac.Pep;
import no.nav.common.client.aktoroppslag.AktorOppslagClient;
import no.nav.common.cxf.CXFClient;
import no.nav.common.featuretoggle.UnleashClient;
import no.nav.common.health.HealthCheckResult;
import no.nav.common.utils.Credentials;
import no.nav.fo.veilarb.dokument.config.EnvironmentProperties;
import no.nav.fo.veilarb.dokument.config.HelsesjekkConfig;
import no.nav.fo.veilarb.dokument.helsesjekk.DokumentproduksjonV3Helsesjekk;
import no.nav.fo.veilarb.dokument.localtestapp.stub.AktorOppslagClientStub;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v3.DokumentproduksjonV3;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static no.nav.fo.veilarb.dokument.localtestapp.stub.Values.*;
import static no.nav.fo.veilarb.dokument.util.Toggles.VEILARBDOKUMENT_ENABLED_TOGGLE;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Configuration
@Import({
        ClientTestConfig.class,
        ServiceTestConfig.class,
        ControllerTestConfig.class,
        FilterTestConfig.class,
        HelsesjekkConfig.class
})
public class ApplicationTestConfig {

    @Bean
    public Credentials serviceUserCredentials() {
        return new Credentials(TEST_SRV_USERNAME, TEST_SRV_PASSWORD);
    }

    @Bean
    public AktorOppslagClient aktorOppslagClient() {
        return new AktorOppslagClientStub();
    }

    @Bean
    public Pep veilarbPep() {
        return mock(Pep.class);
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

    @Bean DokumentproduksjonV3Helsesjekk dokumentproduksjonV3Helsesjekk() {
        EnvironmentProperties environmentProperties = new EnvironmentProperties();
        environmentProperties.setDokumentproduksjonUrl("URL");
        return new DokumentproduksjonV3Helsesjekk(dokumentproduksjonV3(), environmentProperties);
    }

    @Bean
    public UnleashClient unleashClient() {
        UnleashClient unleashClient = mock(UnleashClient.class);
        when(unleashClient.isEnabled(VEILARBDOKUMENT_ENABLED_TOGGLE)).thenReturn(TEST_VEILARBDOKUMENT_ENABLED_TOGGLE);
        when(unleashClient.checkHealth()).thenReturn(HealthCheckResult.healthy());
        return unleashClient;
    }
}
