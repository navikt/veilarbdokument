import no.nav.apiapp.ApiApp;
import no.nav.fo.veilarb.dokument.ApplicationConfig;
import no.nav.sbl.util.EnvironmentUtils;

import static no.nav.brukerdialog.security.Constants.OIDC_REDIRECT_URL_PROPERTY_NAME;
import static no.nav.dialogarena.aktor.AktorConfig.AKTOER_ENDPOINT_URL;
import static no.nav.sbl.dialogarena.common.cxf.StsSecurityConstants.STS_URL_KEY;
import static no.nav.sbl.util.EnvironmentUtils.Type.PUBLIC;

public class Main {

    public static void main(String... args) {

        EnvironmentUtils.setProperty(AKTOER_ENDPOINT_URL, ApplicationConfig.getAktorEndpointUrl(), PUBLIC);
        EnvironmentUtils.setProperty(STS_URL_KEY, ApplicationConfig.getSecurityTokenServiceUrl(), PUBLIC);
        EnvironmentUtils.setProperty(OIDC_REDIRECT_URL_PROPERTY_NAME, ApplicationConfig.getVeilarbloginRedirectUrl(), PUBLIC);

        ApiApp.runApp(ApplicationConfig.class, args);
    }
}
