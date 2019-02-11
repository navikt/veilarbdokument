package no.nav.fo.veilarb.dokument;

import no.nav.testconfig.ApiAppTest;

public class TestConfig {

    private static final String APPLICATION_NAME = "veilarbdokument";

    public static void setupTestContext() {
        ApiAppTest.setupTestContext(ApiAppTest.Config.builder().applicationName(APPLICATION_NAME).build());
    }
}
