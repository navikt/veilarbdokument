package no.nav.fo.veilarb.dokument.localtestapp.config;

import no.nav.fo.veilarb.dokument.client.api.ArenaClient;
import no.nav.fo.veilarb.dokument.client.api.EnhetClient;
import no.nav.fo.veilarb.dokument.client.api.SakClient;
import no.nav.fo.veilarb.dokument.client.api.VeilederClient;
import no.nav.fo.veilarb.dokument.localtestapp.stub.ArenaClientStub;
import no.nav.fo.veilarb.dokument.localtestapp.stub.EnhetClientStub;
import no.nav.fo.veilarb.dokument.localtestapp.stub.SakClientStub;
import no.nav.fo.veilarb.dokument.localtestapp.stub.VeilederClientStub;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("local")
public class ClientTestConfig {

    @Bean
    public ArenaClient arenaClient() {
        return new ArenaClientStub();
    }

    @Bean
    public EnhetClient enhetClient() {
        return new EnhetClientStub();
    }

    @Bean
    public SakClient sakClient() {
        return new SakClientStub();
    }

    @Bean
    public VeilederClient veilederClient() {
        return new VeilederClientStub();
    }
}
