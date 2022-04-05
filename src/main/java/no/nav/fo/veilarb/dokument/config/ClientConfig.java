package no.nav.fo.veilarb.dokument.config;

import no.nav.common.auth.context.AuthContextHolder;
import no.nav.common.client.aktoroppslag.AktorOppslagClient;
import no.nav.common.client.aktoroppslag.CachedAktorOppslagClient;
import no.nav.common.client.aktoroppslag.PdlAktorOppslagClient;
import no.nav.common.client.pdl.PdlClientImpl;
import no.nav.common.rest.client.RestClient;
import no.nav.common.sts.SystemUserTokenProvider;
import no.nav.common.utils.EnvironmentUtils;
import no.nav.fo.veilarb.dokument.client.api.*;
import no.nav.fo.veilarb.dokument.client.impl.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static no.nav.common.utils.UrlUtils.*;
import static no.nav.common.utils.UrlUtils.createNaisAdeoIngressUrl;
import static no.nav.common.utils.UrlUtils.createNaisPreprodIngressUrl;

@Configuration
public class ClientConfig {

    @Bean
    public ArenaClient arenaClient(AuthContextHolder authContextHolder) {
        return new ArenaClientImpl(
                RestClient.baseClient(),
                naisPreprodOrNaisAdeoIngress("veilarbarena", true),
                authContextHolder
        );
    }

    @Bean
    public EnhetClient enhetClient(EnvironmentProperties properties) {
        return new EnhetClientImpl(RestClient.baseClient(), properties.getNorg2Url());
    }

    @Bean
    public SakClient sakClient(AuthContextHolder authContextHolder) {
        return new SakClientImpl(
                RestClient.baseClient(),
                naisPreprodOrNaisAdeoIngress("sak", false),
                authContextHolder
        );
    }

    @Bean
    public VeilederClient veilederClient(AuthContextHolder authContextHolder) {
        return new VeilederClientImpl(
                RestClient.baseClient(),
                naisPreprodOrNaisAdeoIngress("veilarbveileder", true),
                authContextHolder
        );
    }

    @Bean
    public PersonClient personClient(AuthContextHolder authContextHolder) {
        return new PersonClientImpl(
                RestClient.baseClient(),
                naisPreprodOrNaisAdeoIngress("veilarbperson", true),
                authContextHolder
        );
    }

    @Bean
    public BrevClient brevClient() {
        return new BrevClientImpl(RestClient.baseClient(), createServiceUrl("pto-pdfgen", false));
    }

    private static String naisPreprodOrNaisAdeoIngress(String appName, boolean withAppContextPath) {
        return EnvironmentUtils.isProduction().orElseThrow(() -> new IllegalStateException("Unable to find cluster"))
                ? createNaisAdeoIngressUrl(appName, withAppContextPath)
                : createNaisPreprodIngressUrl(appName, "q1", withAppContextPath);
    }

    @Bean
    public AktorOppslagClient aktorOppslagClient(SystemUserTokenProvider systemUserTokenProvider) {
        String url = isProduction()
                ? createProdInternalIngressUrl("pdl-api")
                : createDevInternalIngressUrl("pdl-api-q1");

        PdlClientImpl pdlClient = new PdlClientImpl(
                url,
                systemUserTokenProvider::getSystemUserToken,
                systemUserTokenProvider::getSystemUserToken);

        return new CachedAktorOppslagClient(new PdlAktorOppslagClient(pdlClient));
    }

    private static boolean isProduction() {
        return EnvironmentUtils.isProduction().orElseThrow();
    }
}
