import no.nav.fo.veilarb.dokument.TestConfig;

public class MainTest {

    public static void main(String... args) {
//        System.setProperty("no.nav.sbl.dialogarena.common.cxf.cxfclient.logging.logg-securityheader", "true");
        TestConfig.setupTestContext();
        Main.main("8674");
    }
}
