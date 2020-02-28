package no.nav.fo.veilarb.dokument.util;

import no.nav.common.auth.SsoToken;
import no.nav.common.auth.SubjectHandler;

public class AuthUtils {

    public static String createBearerToken() {
        return SubjectHandler.getSsoToken().map(SsoToken::getToken).map(token -> "Bearer " + token).orElse(null);
    }
}
