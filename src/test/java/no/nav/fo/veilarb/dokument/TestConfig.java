package no.nav.fo.veilarb.dokument;

import no.nav.fasit.ServiceUser;
import no.nav.fasit.WebServiceEndpoint;
import no.nav.fasit.dto.RestService;
import no.nav.sbl.util.EnvironmentUtils;
import no.nav.testconfig.ApiAppTest;

import static no.nav.brukerdialog.security.Constants.*;
import static no.nav.fasit.FasitUtils.*;
import static no.nav.fasit.FasitUtils.Zone.FSS;
import static no.nav.fo.veilarb.dokument.ApplicationConfig.*;
import static no.nav.sbl.dialogarena.common.cxf.StsSecurityConstants.SYSTEMUSER_PASSWORD;
import static no.nav.sbl.dialogarena.common.cxf.StsSecurityConstants.SYSTEMUSER_USERNAME;
import static no.nav.sbl.featuretoggle.unleash.UnleashServiceConfig.UNLEASH_API_URL_PROPERTY_NAME;
import static no.nav.sbl.util.EnvironmentUtils.Type.PUBLIC;
import static no.nav.sbl.util.EnvironmentUtils.Type.SECRET;
import static no.nav.sbl.util.EnvironmentUtils.resolveSrvUserPropertyName;
import static no.nav.sbl.util.EnvironmentUtils.resolverSrvPasswordPropertyName;

public class TestConfig {

    private static final String APPLICATION_NAME = "veilarbdokument";
    private static final String SERVICE_USER_NAME = "srv" + APPLICATION_NAME;

    public static void setupTestContext() {
        ApiAppTest.setupTestContext(ApiAppTest.Config.builder().applicationName(APPLICATION_NAME).build());

        ServiceUser serviceUser = getServiceUser(SERVICE_USER_NAME, APPLICATION_NAME, FSS);
        RestService veilarbLogin = getRestService("veilarblogin.redirect-url");
        ServiceUser issoRpUser = getServiceUser("isso-rp-user", APPLICATION_NAME, FSS);

        EnvironmentUtils.setProperty(SYSTEMUSER_USERNAME, serviceUser.getUsername(), PUBLIC);
        EnvironmentUtils.setProperty(SYSTEMUSER_PASSWORD, serviceUser.getPassword(), SECRET);
        EnvironmentUtils.setProperty(resolveSrvUserPropertyName(), serviceUser.getUsername(), PUBLIC);
        EnvironmentUtils.setProperty(resolverSrvPasswordPropertyName(), serviceUser.getPassword(), SECRET);
        EnvironmentUtils.setProperty(ISSO_HOST_URL_PROPERTY_NAME, getBaseUrl("isso-host"), PUBLIC);
        EnvironmentUtils.setProperty(ISSO_RP_USER_USERNAME_PROPERTY_NAME, issoRpUser.username, PUBLIC);
        EnvironmentUtils.setProperty(ISSO_RP_USER_PASSWORD_PROPERTY_NAME, issoRpUser.password, SECRET);
        EnvironmentUtils.setProperty(ISSO_JWKS_URL_PROPERTY_NAME, getBaseUrl("isso-jwks"), PUBLIC);
        EnvironmentUtils.setProperty(ISSO_ISSUER_URL_PROPERTY_NAME, getBaseUrl("isso-issuer"), PUBLIC);
        EnvironmentUtils.setProperty(OIDC_REDIRECT_URL, veilarbLogin.getUrl(), PUBLIC);
        EnvironmentUtils.setProperty(ISSO_ISALIVE_URL_PROPERTY_NAME, getBaseUrl("isso.isalive", FSS), PUBLIC);
        EnvironmentUtils.setProperty(UNLEASH_API_URL_PROPERTY_NAME, "https://unleash.nais.adeo.no/api/", PUBLIC);

        WebServiceEndpoint dokumentproduksjonEndpoint = getWebServiceEndpoint("Dokumentproduksjon_v3", getDefaultEnvironment());
        EnvironmentUtils.setProperty(
                DOKUMENTPRODUKSJON_ENDPOINT_URL,
                dokumentproduksjonEndpoint.getUrl(),
                PUBLIC);

        WebServiceEndpoint aktorEndpoint = getWebServiceEndpoint("Aktoer_v2", getDefaultEnvironment());
        EnvironmentUtils.setProperty(
                AKTOR_ENDPOINT_URL,
                aktorEndpoint.getUrl(),
                PUBLIC);

        EnvironmentUtils.setProperty(
                SECURITYTOKENSERVICE_URL,
                getBaseUrl("securityTokenService", FSS),
                PUBLIC
                );

        EnvironmentUtils.setProperty("VEILARBABAC", "https://veilarbabac-"+ getDefaultEnvironment()+".nais.preprod.local" , SECRET);
    }
}
