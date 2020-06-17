package no.nav.fo.veilarb.dokument;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

public class TestConfig {

    private static final String TESTMILJO = "q1";

    public static void setupTestContext() {
/*
        loadProperties(".credentials.properties");

        ApiAppTest.setupTestContext(
                ApiAppTest.Config.builder().applicationName(APPLICATION_NAME).environment(TESTMILJO).build());

        String serviceUserUsername = getRequiredProperty(resolveSrvUserPropertyName());
        String serviceUserPassword = getRequiredProperty(resolverSrvPasswordPropertyName());

        setProperty(CredentialConstants.SYSTEMUSER_USERNAME, serviceUserUsername, PUBLIC);
        setProperty(CredentialConstants.SYSTEMUSER_PASSWORD, serviceUserPassword, SECRET);
        setProperty(StsSecurityConstants.SYSTEMUSER_USERNAME, serviceUserUsername, PUBLIC);
        setProperty(StsSecurityConstants.SYSTEMUSER_PASSWORD, serviceUserPassword, SECRET);

        setProperty(OPENAM_DISCOVERY_URL, "https://isso-q.adeo.no/isso/oauth2/.well-known/openid-configuration", PUBLIC);
        setProperty(VEILARBLOGIN_OPENAM_REFRESH_URL, "trengs ikke", PUBLIC);

        setProperty(UNLEASH_API_URL_PROPERTY_NAME, "https://unleash.nais.adeo.no/api/", PUBLIC);
        setProperty(ABAC_ENDPOINT_URL_PROPERTY_NAME, "https://wasapp-" + TESTMILJO + ".adeo.no/asm-pdp/authorize", PUBLIC);
        setProperty(VEILARBVEILEDER_API_URL_PROPERTY, lagFssUrl("veilarbveileder", TESTMILJO, true) + "api/", PUBLIC);
        setProperty(VEILARBARENA_API_URL_PROPERTY, lagFssUrl("veilarbarena", TESTMILJO, true) + "api", PUBLIC);
        setProperty(SAK_API_URL, lagFssUrl("sak", TESTMILJO, false), PUBLIC);
        setProperty(DOKUMENTPRODUKSJON_ENDPOINT_URL, lagFssUrl("dokprod", TESTMILJO, true) + "ws/dokumentproduksjon/v3", PUBLIC);
        setProperty(AKTOR_ENDPOINT_URL, "https://app-" + TESTMILJO + ".adeo.no/aktoerregister/ws/Aktoer/v2", PUBLIC);
        setProperty(SECURITYTOKENSERVICE_URL, "https://sts-" + TESTMILJO + ".preprod.local/SecurityTokenServiceProvider/", PUBLIC);
        setProperty(NORG2_API_URL_PROPERTY, "https://app-" + TESTMILJO + ".adeo.no/norg2/api", PUBLIC);
        */
    }

    private static void loadProperties(String resourcePath) {
        Properties properties = new Properties();
        try (InputStream inputStream = TestConfig.class.getClassLoader().getResourceAsStream(resourcePath)) {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            System.setProperty((String) entry.getKey(), (String) entry.getValue());
        }
    }
}
