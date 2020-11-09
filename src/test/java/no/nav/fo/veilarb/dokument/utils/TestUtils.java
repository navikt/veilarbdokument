package no.nav.fo.veilarb.dokument.utils;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.PlainJWT;
import lombok.SneakyThrows;
import no.nav.common.auth.context.AuthContext;
import no.nav.common.auth.context.UserRole;
import no.nav.fo.veilarb.dokument.service.KontaktEnhetServiceTest;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestUtils {
    @SneakyThrows
    public static String readTestResourceFile(String fileName) {
        URL fileUrl = KontaktEnhetServiceTest.class.getClassLoader().getResource(fileName);
        Path resPath = Paths.get(fileUrl.toURI());
        return new String(Files.readAllBytes(resPath), StandardCharsets.UTF_8);
    }

    public static AuthContext authContext(String subject) {
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(subject)
                .build();

        JWT jwt = new PlainJWT(claimsSet);

        return new AuthContext(UserRole.INTERN, jwt);
    }

    public static void givenWiremockOkJsonResponse(String url, String json) {
        WireMock.givenThat(
                WireMock.get(WireMock.urlEqualTo(url))
                        .willReturn(WireMock.aResponse()
                                .withStatus(200)
                                .withHeader("Content-Type", "applicaition/json")
                                .withBody(json)
                        ));
    }
}
