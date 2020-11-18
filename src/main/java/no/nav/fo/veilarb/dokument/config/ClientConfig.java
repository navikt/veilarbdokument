package no.nav.fo.veilarb.dokument.config;

import no.nav.common.rest.client.RestClient;
import no.nav.common.utils.EnvironmentUtils;
import no.nav.fo.veilarb.dokument.client.api.*;
import no.nav.fo.veilarb.dokument.client.impl.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static no.nav.common.utils.UrlUtils.*;

@Configuration
public class ClientConfig {

    @Bean
    public ArenaClient arenaClient() {
        return new ArenaClientImpl(RestClient.baseClient(), naisPreprodOrNaisAdeoIngress("veilarbarena", true));
    }

    @Bean
    public EnhetClient enhetClient(EnvironmentProperties properties) {
        return new EnhetClientImpl(RestClient.baseClient(), properties.getNorg2Url());
    }

    @Bean
    public SakClient sakClient() {
        return new SakClientImpl(RestClient.baseClient(), naisPreprodOrNaisAdeoIngress("sak", false));
    }

    @Bean
    public BrevClient brevClient() {
        return new BrevClientImpl(RestClient.baseClient(), createServiceUrl("pto-pdfgen", false));
    }

    @Bean
    public PersonClient personClient() {
        return new PersonClientImpl(RestClient.baseClient(), naisPreprodOrNaisAdeoIngress("veilarbperson", true));
    }

    @Bean
    public VeilederClient veilederClient() {
        return new VeilederClientImpl(RestClient.baseClient(), naisPreprodOrNaisAdeoIngress("veilarbveileder", true));
    }

    private static String naisPreprodOrNaisAdeoIngress(String appName, boolean withAppContextPath) {
        return EnvironmentUtils.isProduction().orElseThrow(() -> new IllegalStateException("Unable to find cluster"))
                ? createNaisAdeoIngressUrl(appName, withAppContextPath)
                : createNaisPreprodIngressUrl(appName, "q1", withAppContextPath);
    }

}
