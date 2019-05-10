package no.nav.fo.veilarb.dokument.utils;

import no.nav.fasit.FasitUtils;

public class TestUtils {

    private TestUtils(){}

    public static String lagFssUrl (String appName, boolean withAppContext) {
        String url = withAppContext ? "https://%s-%s.nais.preprod.local/%s/" : "https://%s-%s.nais.preprod.local/";
        return String.format(url, appName, FasitUtils.getDefaultEnvironment(), appName);
    }
}
