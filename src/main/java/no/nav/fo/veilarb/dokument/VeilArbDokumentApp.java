package no.nav.fo.veilarb.dokument;

import no.nav.common.utils.SslUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VeilArbDokumentApp {

    public static void main(String... args) {
        SslUtils.setupTruststore();
        SpringApplication.run(VeilArbDokumentApp.class, args);
    }
}
