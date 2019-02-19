package no.nav.fo.veilarb.dokument;

import no.nav.sbl.util.EnvironmentUtils;
import no.nav.testconfig.ApiAppTest;

import static no.nav.dialogarena.config.fasit.FasitUtils.getDefaultEnvironment;
import static no.nav.dialogarena.config.fasit.FasitUtils.getWebServiceEndpoint;
import static no.nav.fo.veilarb.dokument.ApplicationConfig.DOKUMENTPRODUKSJON_ENDPOINT_URL;
import static no.nav.sbl.util.EnvironmentUtils.Type.PUBLIC;

public class TestConfig {

    private static final String APPLICATION_NAME = "veilarbdokument";

    public static void setupTestContext() {
        ApiAppTest.setupTestContext(ApiAppTest.Config.builder().applicationName(APPLICATION_NAME).build());

        EnvironmentUtils.setProperty(
                DOKUMENTPRODUKSJON_ENDPOINT_URL,
                getWebServiceEndpoint("Dokumentproduksjon_v3", getDefaultEnvironment()).getUrl(),
                PUBLIC);
    }
}
