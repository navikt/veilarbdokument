package no.nav.fo.veilarb.dokument.localtestapp.config;

import no.nav.fo.veilarb.dokument.client.api.*;
import no.nav.fo.veilarb.dokument.localtestapp.stub.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
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

    @Bean
    public BrevClient brevClient() {
        return new BrevClientStub();
    }

    @Bean
    public PersonClient personClient() {
        return new PersonClientStub();
    }
}
