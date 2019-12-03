package no.nav.fo.veilarb.dokument;

import no.nav.fasit.ServiceUser;
import no.nav.fasit.WebServiceEndpoint;
import no.nav.fasit.dto.RestService;
import no.nav.testconfig.ApiAppTest;

import static no.nav.brukerdialog.security.Constants.*;
import static no.nav.fasit.FasitUtils.*;
import static no.nav.fasit.FasitUtils.Zone.FSS;
import static no.nav.fo.veilarb.dokument.ApplicationConfig.*;
import static no.nav.fo.veilarb.dokument.utils.TestUtils.lagFssUrl;
import static no.nav.sbl.dialogarena.common.abac.pep.service.AbacServiceConfig.ABAC_ENDPOINT_URL_PROPERTY_NAME;
import static no.nav.sbl.dialogarena.common.cxf.StsSecurityConstants.SYSTEMUSER_PASSWORD;
import static no.nav.sbl.dialogarena.common.cxf.StsSecurityConstants.SYSTEMUSER_USERNAME;
import static no.nav.sbl.featuretoggle.unleash.UnleashServiceConfig.UNLEASH_API_URL_PROPERTY_NAME;
import static no.nav.sbl.util.EnvironmentUtils.*;
import static no.nav.sbl.util.EnvironmentUtils.Type.PUBLIC;
import static no.nav.sbl.util.EnvironmentUtils.Type.SECRET;

public class TestConfig {

    private static final String APPLICATION_NAME = "veilarbdokument";
    private static final String SERVICE_USER_NAME = "srv" + APPLICATION_NAME;

    public static void setupTestContext() {
        ApiAppTest.setupTestContext(ApiAppTest.Config.builder().applicationName(APPLICATION_NAME).build());

        ServiceUser serviceUser = getServiceUser(SERVICE_USER_NAME, APPLICATION_NAME, FSS);
        RestService veilarbLogin = getRestService("veilarblogin.redirect-url");
        ServiceUser issoRpUser = getServiceUser("isso-rp-user", APPLICATION_NAME, FSS);
        RestService sak = getRestService("sak.saker");

        setProperty(SYSTEMUSER_USERNAME, serviceUser.getUsername(), PUBLIC);
        setProperty(SYSTEMUSER_PASSWORD, serviceUser.getPassword(), SECRET);
        setProperty(resolveSrvUserPropertyName(), serviceUser.getUsername(), PUBLIC);
        setProperty(resolverSrvPasswordPropertyName(), serviceUser.getPassword(), SECRET);
        setProperty(ISSO_HOST_URL_PROPERTY_NAME, getBaseUrl("isso-host"), PUBLIC);
        setProperty(ISSO_RP_USER_USERNAME_PROPERTY_NAME, issoRpUser.username, PUBLIC);
        setProperty(ISSO_RP_USER_PASSWORD_PROPERTY_NAME, issoRpUser.password, SECRET);
        setProperty(ISSO_JWKS_URL_PROPERTY_NAME, getBaseUrl("isso-jwks"), PUBLIC);
        setProperty(ISSO_ISSUER_URL_PROPERTY_NAME, getBaseUrl("isso-issuer"), PUBLIC);
        setProperty(OIDC_REDIRECT_URL, veilarbLogin.getUrl(), PUBLIC);
        setProperty(ISSO_ISALIVE_URL_PROPERTY_NAME, getBaseUrl("isso.isalive", FSS), PUBLIC);
        setProperty(UNLEASH_API_URL_PROPERTY_NAME, "https://unleash.nais.adeo.no/api/", PUBLIC);
        setProperty(VEILARBABAC_API_URL_PROPERTY, lagFssUrl("veilarbabac", false), PUBLIC);
        setProperty(ABAC_ENDPOINT_URL_PROPERTY_NAME, "https://wasapp-" + getDefaultEnvironment() + ".adeo.no/asm-pdp/authorize", PUBLIC);
        setProperty(VEILARBARENA_API_URL_PROPERTY, lagFssUrl("veilarbarena", true) + "api/", PUBLIC);
        setProperty(VEILARBVEILEDER_API_URL_PROPERTY, lagFssUrl("veilarbveileder", true) + "api/", PUBLIC);

        if (requireNamespace().equals("q1")) {
            setProperty(SAK_API_URL, "https://sak-q1.nais.preprod.local/api/v1/saker", PUBLIC);
        } else {
            setProperty(SAK_API_URL, sak.getUrl(), PUBLIC);
        }

        WebServiceEndpoint dokumentproduksjonEndpoint = getWebServiceEndpoint("Dokumentproduksjon_v3", getDefaultEnvironment());
        setProperty(
                DOKUMENTPRODUKSJON_ENDPOINT_URL,
                dokumentproduksjonEndpoint.getUrl(),
                PUBLIC);

        WebServiceEndpoint aktorEndpoint = getWebServiceEndpoint("Aktoer_v2", getDefaultEnvironment());
        setProperty(
                AKTOR_ENDPOINT_URL,
                aktorEndpoint.getUrl(),
                PUBLIC);

        setProperty(
                SECURITYTOKENSERVICE_URL,
                getBaseUrl("securityTokenService", FSS),
                PUBLIC
        );

        setProperty("VEILARBABAC", "https://veilarbabac-" + getDefaultEnvironment() + ".nais.preprod.local", PUBLIC);
    }
}
