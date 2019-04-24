import no.nav.fo.veilarb.dokument.TestConfig;

public class MainTest {

    public static void main(String... args) {
        TestConfig.setupTestContext();
        Main.main("8674");
    }
}
