package no.nav.fo.veilarb.dokument;

import no.nav.apiapp.ApiApplication;
import no.nav.apiapp.config.ApiAppConfigurator;
import no.nav.brukerdialog.security.oidc.SystemUserTokenProvider;
import no.nav.brukerdialog.security.oidc.SystemUserTokenProviderConfig;
import no.nav.dialogarena.aktor.AktorConfig;
import no.nav.fo.veilarb.dokument.service.DokumentService;
import no.nav.fo.veilarb.dokument.utils.VeilarbAbacServiceClient;
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
        VeilarbAbacServiceClient.class,
        AktorConfig.class
})
public class ApplicationConfig implements ApiApplication {

    public static final String DOKUMENTPRODUKSJON_ENDPOINT_URL = "DOKUMENTPRODUKSJON_V3_ENDPOINT_URL";
    public static final String AKTOR_ENDPOINT_URL = "AKTOER_V2_ENDPOINT_URL";
    public static final String SECURITYTOKENSERVICE_URL = "SECURITYTOKENSERVICE_URL";
    public static final String OIDC_REDIRECT_URL = "OIDC_REDIRECT_URL";

    @Override
    public void configure(ApiAppConfigurator apiAppConfigurator) {
        apiAppConfigurator
                .addPublicPath("/*")
                .issoLogin();
    }

    @Bean
    public DokumentproduksjonV3 dokumentproduksjonV3() {
        return new CXFClient<>(DokumentproduksjonV3.class)
                .address(getDokumentproduksjonEndpointUrl())
                .configureStsForSystemUser()
                .build();
    }

    @Bean
    public SystemUserTokenProvider systemUserTokenProvider() {
        return new SystemUserTokenProvider(SystemUserTokenProviderConfig.resolveFromSystemProperties());
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
}
