package no.nav.fo.veilarb.dokument.utils;

public class TestUtils {

    private TestUtils(){}

    public static String lagFssUrl (String appName, String testmiljo, boolean withAppContext) {
        String url = withAppContext ? "https://%s-%s.nais.preprod.local/%s/" : "https://%s-%s.nais.preprod.local/";
        return String.format(url, appName, testmiljo, appName);
    }
}
