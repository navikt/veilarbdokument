package no.nav.fo.veilarb.dokument;

import no.nav.dialogarena.config.fasit.FasitUtils;
import no.nav.dialogarena.config.fasit.ServiceUser;
import no.nav.dialogarena.config.fasit.WebServiceEndpoint;
import no.nav.sbl.util.EnvironmentUtils;
import no.nav.testconfig.ApiAppTest;

import static no.nav.dialogarena.config.fasit.FasitUtils.*;
import static no.nav.dialogarena.config.fasit.FasitUtils.Zone.*;
import static no.nav.fo.veilarb.dokument.ApplicationConfig.*;
import static no.nav.sbl.util.EnvironmentUtils.Type.PUBLIC;
import static no.nav.sbl.util.EnvironmentUtils.resolveSrvUserPropertyName;
import static no.nav.sbl.util.EnvironmentUtils.resolverSrvPasswordPropertyName;
import static no.nav.sbl.dialogarena.common.cxf.StsSecurityConstants.SYSTEMUSER_PASSWORD;
import static no.nav.sbl.dialogarena.common.cxf.StsSecurityConstants.SYSTEMUSER_USERNAME;

public class TestConfig {

    private static final String APPLICATION_NAME = "veilarbdokument";
    private static final String SERVICE_USER_NAME = "srv" + APPLICATION_NAME;

    public static void setupTestContext() {
        ApiAppTest.setupTestContext(ApiAppTest.Config.builder().applicationName(APPLICATION_NAME).build());

        ServiceUser serviceUser = FasitUtils.getServiceUser(SERVICE_USER_NAME, APPLICATION_NAME, FSS);
        EnvironmentUtils.setProperty(SYSTEMUSER_USERNAME, serviceUser.getUsername(), PUBLIC);
        EnvironmentUtils.setProperty(SYSTEMUSER_PASSWORD, serviceUser.getPassword(), PUBLIC);
        EnvironmentUtils.setProperty(resolveSrvUserPropertyName(), serviceUser.getUsername(), PUBLIC);
        EnvironmentUtils.setProperty(resolverSrvPasswordPropertyName(), serviceUser.getPassword(), PUBLIC);

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
    }
}
