package no.nav.fo.veilarb.dokument.util;

import no.nav.common.auth.context.AuthContextHolder;

public class AuthUtils {

    public static String createBearerToken() {
        return AuthContextHolder.getIdTokenString().map(token -> "Bearer " + token).orElse(null);
    }
}
