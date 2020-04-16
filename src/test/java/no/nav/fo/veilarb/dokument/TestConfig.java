package no.nav.fo.veilarb.dokument;

import no.nav.common.nais.utils.NaisUtils;
import no.nav.sbl.dialogarena.common.abac.pep.CredentialConstants;
import no.nav.sbl.dialogarena.common.cxf.StsSecurityConstants;
import no.nav.testconfig.ApiAppTest;

import static no.nav.fo.veilarb.dokument.config.ApplicationConfig.*;
import static no.nav.fo.veilarb.dokument.utils.TestUtils.lagFssUrl;
import static no.nav.sbl.dialogarena.common.abac.pep.service.AbacServiceConfig.ABAC_ENDPOINT_URL_PROPERTY_NAME;
import static no.nav.sbl.featuretoggle.unleash.UnleashServiceConfig.UNLEASH_API_URL_PROPERTY_NAME;
import static no.nav.sbl.util.EnvironmentUtils.*;
import static no.nav.sbl.util.EnvironmentUtils.Type.PUBLIC;
import static no.nav.sbl.util.EnvironmentUtils.Type.SECRET;

public class TestConfig {

    private static final String APPLICATION_NAME = "veilarbdokument";
    private static final String SERVICE_USER_NAME = "srv" + APPLICATION_NAME;
    private static final String TESTMILJO = "q1";

    public static void setupTestContext() {
        ApiAppTest.setupTestContext(ApiAppTest.Config.builder().applicationName(APPLICATION_NAME).build());

        NaisUtils.Credentials serviceUser = null; // TODO

        setProperty(resolveSrvUserPropertyName(), serviceUser.username, PUBLIC);
        setProperty(resolverSrvPasswordPropertyName(), serviceUser.password, SECRET);
        setProperty(CredentialConstants.SYSTEMUSER_USERNAME, serviceUser.username, PUBLIC);
        setProperty(CredentialConstants.SYSTEMUSER_PASSWORD, serviceUser.password, SECRET);
        setProperty(StsSecurityConstants.SYSTEMUSER_USERNAME, serviceUser.username, PUBLIC);
        setProperty(StsSecurityConstants.SYSTEMUSER_PASSWORD, serviceUser.password, SECRET);

        setProperty(OPENAM_DISCOVERY_URL, "https://isso-q.adeo.no/isso/oauth2/.well-known/openid-configuration", PUBLIC);
        setProperty(VEILARBLOGIN_OPENAM_CLIENT_ID, "TODO", PUBLIC);
        setProperty(VEILARBLOGIN_OPENAM_REFRESH_URL, "trengs ikke", PUBLIC);

        setProperty(UNLEASH_API_URL_PROPERTY_NAME, "https://unleash.nais.adeo.no/api/", PUBLIC);
        setProperty(ABAC_ENDPOINT_URL_PROPERTY_NAME, "https://wasapp-" + TESTMILJO + ".adeo.no/asm-pdp/authorize", PUBLIC);
        setProperty(VEILARBVEILEDER_API_URL_PROPERTY, lagFssUrl("veilarbveileder", TESTMILJO, true) + "api/", PUBLIC);
        setProperty(VEILARBARENA_API_URL_PROPERTY, lagFssUrl("veilarbarena", TESTMILJO, true) + "api", PUBLIC);
        setProperty(SAK_API_URL, lagFssUrl("sak", TESTMILJO, false), PUBLIC);
        setProperty(DOKUMENTPRODUKSJON_ENDPOINT_URL, lagFssUrl("dokprod", TESTMILJO, true) + "ws/dokumentproduksjon/v3", PUBLIC);
        setProperty(AKTOR_ENDPOINT_URL, "https://app-" + TESTMILJO + ".adeo.no/aktoerregister/ws/Aktoer/v2", PUBLIC);
        setProperty(SECURITYTOKENSERVICE_URL, "https://sts-" + TESTMILJO + ".preprod.local/SecurityTokenServiceProvider/", PUBLIC);
    }
}
