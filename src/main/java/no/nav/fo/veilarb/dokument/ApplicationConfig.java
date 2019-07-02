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
import no.nav.sbl.dialogarena.common.cxf.StsSecurityConstants;
import no.nav.sbl.featuretoggle.unleash.UnleashService;
import no.nav.sbl.rest.RestUtils;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v3.DokumentproduksjonV3;
import no.nav.virksomhet.tjenester.sak.arbeidogaktivitet.v1.ArbeidOgAktivitet;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.apache.wss4j.common.ext.WSPasswordCallback;
import org.apache.wss4j.dom.WSConstants;
import org.apache.wss4j.dom.handler.WSHandlerConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.security.auth.callback.CallbackHandler;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import java.util.HashMap;
import java.util.Map;

import static java.lang.System.getProperty;
import static no.nav.sbl.featuretoggle.unleash.UnleashServiceConfig.resolveFromEnvironment;
import static no.nav.sbl.util.EnvironmentUtils.getRequiredProperty;

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
        ArenaSakService.class,
        OppfolgingssakService.class
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
    public static final String ARBEID_OG_AKTIVITET_URL_PROPERTY = "VIRKSOMHET_ARBEIDOGAKTIVITET_V1_ENDPOINTURL";

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
        String username = getRequiredProperty(StsSecurityConstants.SYSTEMUSER_USERNAME);
        String password = getRequiredProperty(StsSecurityConstants.SYSTEMUSER_PASSWORD);

        Map<String, Object> props = new HashMap<>();
        props.put(WSHandlerConstants.ACTION, WSHandlerConstants.USERNAME_TOKEN);
        props.put(WSHandlerConstants.USER, username);
        props.put(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PW_TEXT);
        props.put(WSHandlerConstants.PW_CALLBACK_REF, (CallbackHandler) callbacks -> {
            WSPasswordCallback passwordCallback = (WSPasswordCallback)callbacks[0];
            passwordCallback.setPassword(password);
        });
        return new CXFClient<>(ArbeidOgAktivitet.class)
                .address(getArbeidOgAktivitetUrl())
                .withOutInterceptor(new WSS4JOutInterceptor(props))
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
        return getRequiredProperty(DOKUMENTPRODUKSJON_ENDPOINT_URL);
    }

    public static String getAktorEndpointUrl() {
        return getRequiredProperty(AKTOR_ENDPOINT_URL);
    }

    public static String getSecurityTokenServiceUrl() {
        return getRequiredProperty(SECURITYTOKENSERVICE_URL);
    }

    public static String getOidcRedirectUrl() {
        return getRequiredProperty(OIDC_REDIRECT_URL);
    }

    public static String getArbeidOgAktivitetUrl() {
        return  getRequiredProperty(ARBEID_OG_AKTIVITET_URL_PROPERTY);
    }
}
