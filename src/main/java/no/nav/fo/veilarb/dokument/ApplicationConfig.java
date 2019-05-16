package no.nav.fo.veilarb.dokument;

import no.nav.apiapp.ApiApplication;
import no.nav.apiapp.config.ApiAppConfigurator;
import no.nav.apiapp.security.veilarbabac.VeilarbAbacPepClient;
import no.nav.brukerdialog.security.oidc.SystemUserTokenProvider;
import no.nav.brukerdialog.security.oidc.SystemUserTokenProviderConfig;
import no.nav.common.auth.Subject;
import no.nav.common.auth.SubjectHandler;
import no.nav.dialogarena.aktor.AktorConfig;
import no.nav.fo.veilarb.dokument.service.*;
import no.nav.sbl.dialogarena.common.abac.pep.Pep;
import no.nav.sbl.dialogarena.common.abac.pep.context.AbacContext;
import no.nav.sbl.dialogarena.common.cxf.CXFClient;
import no.nav.sbl.featuretoggle.unleash.UnleashService;
import no.nav.sbl.rest.RestUtils;
import no.nav.sbl.util.EnvironmentUtils;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v3.DokumentproduksjonV3;
import no.nav.virksomhet.tjenester.sak.arbeidogaktivitet.v1.ArbeidOgAktivitet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;

import static java.lang.System.getProperty;
import static no.nav.sbl.featuretoggle.unleash.UnleashServiceConfig.resolveFromEnvironment;

@Configuration
@Import({
        DokumentRessurs.class,
        DokumentService.class,
        AuthService.class,
        ArenaService.class,
        AktorConfig.class,
        SakService.class,
        VeilederService.class,
        AbacContext.class,
        ArenaSakService.class
})
public class ApplicationConfig implements ApiApplication {

    public static final String DOKUMENTPRODUKSJON_ENDPOINT_URL = "DOKUMENTPRODUKSJON_V3_ENDPOINTURL";
    public static final String AKTOR_ENDPOINT_URL = "AKTOER_V2_ENDPOINTURL";
    public static final String SECURITYTOKENSERVICE_URL = "SECURITYTOKENSERVICE_URL";
    public static final String OIDC_REDIRECT_URL = "OIDC_REDIRECT_URL";
    public static final String SAK_API_URL = "SAK_API_URL";
    public static final String VEILARBABAC_API_URL_PROPERTY = "VEILARBABAC_API_URL";
    public static final String VEILARBARENA_API_URL_PROPERTY = "VEILARBARENAAPI_URL";
    public static final String VEILARBVEILEDER_API_URL_PROPERTY = "VEILARBVEILEDERAPI_URL";
    public static final String TJENESTEBUSS_URL_PROPERTY = "TJENESTEBUSS_URL";

    @Override
    public void configure(ApiAppConfigurator apiAppConfigurator) {
        apiAppConfigurator
                .issoLogin()
                .sts();
    }

    @Bean
    public DokumentproduksjonV3 dokumentproduksjonV3() {
        return new CXFClient<>(DokumentproduksjonV3.class)
                .address(getDokumentproduksjonEndpointUrl())
                .configureStsForSystemUser()
                .build();
    }

    @Bean
    ArbeidOgAktivitet arbeidOgAktivitet() {
        return new CXFClient<>(ArbeidOgAktivitet.class)
                .address(getTjenestebussUrl())
                .configureStsForSystemUser()
                .build();
    }

    @Bean
    public SystemUserTokenProvider systemUserTokenProvider() {
        return new SystemUserTokenProvider(SystemUserTokenProviderConfig.resolveFromSystemProperties());
    }

    @Bean
    public UnleashService unleashService() {
        return new UnleashService(resolveFromEnvironment());
    }

    @Bean
    public Client client() {
        Client client = RestUtils.createClient();
        client.register(new SubjectOidcTokenFilter());
        return client;
    }

    private static class SubjectOidcTokenFilter implements ClientRequestFilter {
        @Override
        public void filter(ClientRequestContext requestContext) {
            SubjectHandler.getSubject()
                    .map(Subject::getSsoToken)
                    .ifPresent(ssoToken ->
                            requestContext.getHeaders().putSingle("Authorization", "Bearer " + ssoToken.getToken()));

        }
    }


    @Bean
    public VeilarbAbacPepClient pepClient(Pep pep) {

        String overrideUrl = getProperty(VEILARBABAC_API_URL_PROPERTY);

        VeilarbAbacPepClient.Builder builder = VeilarbAbacPepClient.ny()
                .medPep(pep)
                .medSystemUserTokenProvider(() -> systemUserTokenProvider().getToken())
                .brukAktoerId(() -> true)
                .sammenlikneTilgang(() -> false)
                .foretrekkVeilarbAbacResultat(() -> true);

        if (overrideUrl != null) {
            return builder
                    .medVeilarbAbacUrl(overrideUrl)
                    .bygg();
        } else {
            return builder.bygg();
        }
    }

    public static String getDokumentproduksjonEndpointUrl() {
        return EnvironmentUtils.getRequiredProperty(DOKUMENTPRODUKSJON_ENDPOINT_URL);
    }

    public static String getAktorEndpointUrl() {
        return EnvironmentUtils.getRequiredProperty(AKTOR_ENDPOINT_URL);
    }

    public static String getSecurityTokenServiceUrl() {
        return EnvironmentUtils.getRequiredProperty(SECURITYTOKENSERVICE_URL);
    }

    public static String getOidcRedirectUrl() {
        return EnvironmentUtils.getRequiredProperty(OIDC_REDIRECT_URL);
    }

    public static String getTjenestebussUrl() {
        return  EnvironmentUtils.getRequiredProperty(TJENESTEBUSS_URL_PROPERTY);
    }
}
