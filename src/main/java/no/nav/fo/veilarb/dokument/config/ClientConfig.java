package no.nav.fo.veilarb.dokument.config;

import no.nav.common.rest.client.RestClient;
import no.nav.fo.veilarb.dokument.client.api.ArenaClient;
import no.nav.fo.veilarb.dokument.client.api.EnhetClient;
import no.nav.fo.veilarb.dokument.client.api.SakClient;
import no.nav.fo.veilarb.dokument.client.api.VeilederClient;
import no.nav.fo.veilarb.dokument.client.impl.ArenaClientImpl;
import no.nav.fo.veilarb.dokument.client.impl.EnhetClientImpl;
import no.nav.fo.veilarb.dokument.client.impl.SakClientImpl;
import no.nav.fo.veilarb.dokument.client.impl.VeilederClientImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

import static no.nav.common.utils.UrlUtils.clusterUrlForApplication;

@Configuration
public class ClientConfig {

    @Bean
    public ArenaClient arenaClient(EnvironmentProperties properties) {
        return new ArenaClientImpl(RestClient.baseClient(),
                Optional.ofNullable(properties.getVeilarbarenaUrl()).orElseGet(
                        () -> clusterUrlForApplication("veilarbarena")));
    }

    @Bean
    public EnhetClient enhetClient(EnvironmentProperties properties) {
        return new EnhetClientImpl(RestClient.baseClient(), properties.getNorg2Url());
    }

    @Bean
    public SakClient sakClient(EnvironmentProperties properties) {
        return new SakClientImpl(RestClient.baseClient(),
                Optional.ofNullable(properties.getSakUrl()).orElseGet(() -> clusterUrlForApplication("sak")));
    }

    @Bean
    public VeilederClient veilederClient(EnvironmentProperties properties) {
        return new VeilederClientImpl(RestClient.baseClient(),
                Optional.ofNullable(properties.getVeilarbarenaUrl()).orElseGet(
                        () -> clusterUrlForApplication("veilarbveileder")));
    }
}
