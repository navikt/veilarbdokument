package no.nav.fo.veilarb.dokument.config;

import no.nav.common.rest.client.RestClient;
import no.nav.fo.veilarb.dokument.client.ArenaClient;
import no.nav.fo.veilarb.dokument.client.EnhetClient;
import no.nav.fo.veilarb.dokument.client.SakClient;
import no.nav.fo.veilarb.dokument.client.VeilederClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

import static no.nav.common.utils.UrlUtils.clusterUrlForApplication;

@Configuration
public class ClientConfig {

    @Bean
    public ArenaClient arenaClient(EnvironmentProperties properties) {
        return new ArenaClient(RestClient.baseClient(),
                Optional.ofNullable(properties.getVeilarbarenaUrl()).orElseGet(
                        () -> clusterUrlForApplication("veilarbarena")));
    }

    @Bean
    public EnhetClient enhetClient(EnvironmentProperties properties) {
        return new EnhetClient(RestClient.baseClient(), properties.getNorg2Url());
    }

    @Bean
    public SakClient sakClient(EnvironmentProperties properties) {
        return new SakClient(RestClient.baseClient(),
                Optional.ofNullable(properties.getSakUrl()).orElseGet(() -> clusterUrlForApplication("sak")));
    }

    @Bean
    public VeilederClient veilederClient(EnvironmentProperties properties) {
        return new VeilederClient(RestClient.baseClient(),
                Optional.ofNullable(properties.getVeilarbarenaUrl()).orElseGet(
                        () -> clusterUrlForApplication("veilarbveileder")));
    }
}
