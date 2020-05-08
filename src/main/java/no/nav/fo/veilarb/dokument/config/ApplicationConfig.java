package no.nav.fo.veilarb.dokument.config;

import no.nav.apiapp.ApiApplication;
import no.nav.apiapp.config.ApiAppConfigurator;
import no.nav.apiapp.config.StsConfig;
import no.nav.apiapp.security.PepClient;
import no.nav.brukerdialog.security.domain.IdentType;
import no.nav.common.oidc.auth.OidcAuthenticatorConfig;
import no.nav.dialogarena.aktor.AktorConfig;
import no.nav.fo.veilarb.dokument.client.SakClient;
import no.nav.fo.veilarb.dokument.resource.DokumentRessurs;
import no.nav.fo.veilarb.dokument.client.ArenaClient;
import no.nav.fo.veilarb.dokument.client.EnhetClient;
import no.nav.fo.veilarb.dokument.client.VeilederClient;
import no.nav.fo.veilarb.dokument.helsesjekk.DokumentproduksjonV3Helsesjekk;
import no.nav.fo.veilarb.dokument.service.*;
import no.nav.sbl.dialogarena.common.abac.pep.Pep;
import no.nav.sbl.dialogarena.common.abac.pep.context.AbacContext;
import no.nav.sbl.dialogarena.common.abac.pep.domain.ResourceType;
import no.nav.sbl.dialogarena.common.cxf.CXFClient;
import no.nav.sbl.dialogarena.common.cxf.StsSecurityConstants;
import no.nav.sbl.featuretoggle.unleash.UnleashService;
import no.nav.sbl.featuretoggle.unleash.UnleashServiceConfig;
import no.nav.sbl.rest.RestUtils;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v3.DokumentproduksjonV3;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.ws.rs.client.Client;

import static no.nav.common.oidc.Constants.OPEN_AM_ID_TOKEN_COOKIE_NAME;
import static no.nav.common.oidc.Constants.REFRESH_TOKEN_COOKIE_NAME;
import static no.nav.sbl.featuretoggle.unleash.UnleashServiceConfig.UNLEASH_API_URL_PROPERTY_NAME;
import static no.nav.sbl.util.EnvironmentUtils.*;

@Configuration
@Import({
        AbacContext.class,
        AktorConfig.class,
        ArenaClient.class,
        SakClient.class,
        VeilederClient.class,
        EnhetClient.class,
        AuthService.class,
        DokumentproduksjonV3Helsesjekk.class,
        DokumentService.class,
        OppfolgingssakService.class,
        KontaktEnhetService.class,
        DokumentRessurs.class,
        CacheConfig.class
})
public class ApplicationConfig implements ApiApplication {

    public static final String DOKUMENTPRODUKSJON_ENDPOINT_URL = "DOKUMENTPRODUKSJON_V3_ENDPOINTURL";
    public static final String AKTOR_ENDPOINT_URL = "AKTOER_V2_ENDPOINTURL";
    public static final String SECURITYTOKENSERVICE_URL = "SECURITYTOKENSERVICE_URL";
    public static final String SAK_API_URL = "SAK_API_URL";
    public static final String VEILARBARENA_API_URL_PROPERTY = "VEILARBARENAAPI_URL";
    public static final String VEILARBVEILEDER_API_URL_PROPERTY = "VEILARBVEILEDER_API_URL";
    public static final String NORG2_API_URL_PROPERTY = "NORG2_API_URL";
    public static final String OPENAM_DISCOVERY_URL = "OPENAM_DISCOVERY_URL";
    public static final String VEILARBLOGIN_OPENAM_CLIENT_ID = "VEILARBLOGIN_OPENAM_CLIENT_ID";
    public static final String VEILARBLOGIN_OPENAM_REFRESH_URL = "VEILARBLOGIN_OPENAM_REFRESH_URL";

    @Override
    public void configure(ApiAppConfigurator apiAppConfigurator) {
        apiAppConfigurator
                .addOidcAuthenticator(createOpenAmAuthenticatorConfig())
                // For CXFClient:
                .sts(createStsConfig());
    }

    private OidcAuthenticatorConfig createOpenAmAuthenticatorConfig() {
        String discoveryUrl = getRequiredProperty(OPENAM_DISCOVERY_URL);
        String clientId = getRequiredProperty(VEILARBLOGIN_OPENAM_CLIENT_ID);
        String refreshUrl = getRequiredProperty(VEILARBLOGIN_OPENAM_REFRESH_URL);

        return new OidcAuthenticatorConfig()
                .withDiscoveryUrl(discoveryUrl)
                .withClientId(clientId)
                .withRefreshUrl(refreshUrl)
                .withRefreshTokenCookieName(REFRESH_TOKEN_COOKIE_NAME)
                .withIdTokenCookieName(OPEN_AM_ID_TOKEN_COOKIE_NAME)
                .withIdentType(IdentType.InternBruker);
    }

    private StsConfig createStsConfig() {
        return StsConfig.builder()
                .url(getRequiredProperty(SECURITYTOKENSERVICE_URL))
                .username(getRequiredProperty(StsSecurityConstants.SYSTEMUSER_USERNAME))
                .password(getRequiredProperty(StsSecurityConstants.SYSTEMUSER_PASSWORD))
                .build();
    }

    @Bean
    public DokumentproduksjonV3 dokumentproduksjonV3() {
        return new CXFClient<>(DokumentproduksjonV3.class)
                .address(getDokumentproduksjonEndpointUrl())
                .configureStsForSystemUser()
                .build();
    }

    @Bean
    public UnleashService unleashService() {
        return new UnleashService(UnleashServiceConfig.builder()
                .applicationName(requireApplicationName())
                .unleashApiUrl(getRequiredProperty(UNLEASH_API_URL_PROPERTY_NAME))
                .build());
    }

    @Bean
    public Client client() {
        return RestUtils.createClient();
    }

    @Bean
    public PepClient pepClient(Pep pep) {
        return new PepClient(pep, "veilarb", ResourceType.VeilArbPerson);
    }

    public static String getDokumentproduksjonEndpointUrl() {
        return getRequiredProperty(DOKUMENTPRODUKSJON_ENDPOINT_URL);
    }
}
