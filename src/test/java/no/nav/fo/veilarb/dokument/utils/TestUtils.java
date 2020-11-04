package no.nav.fo.veilarb.dokument.utils;

import lombok.SneakyThrows;
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
}
