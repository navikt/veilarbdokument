package no.nav.fo.veilarb.dokument.client;

import lombok.extern.slf4j.Slf4j;
import no.nav.apiapp.selftest.Helsesjekk;
import no.nav.apiapp.selftest.HelsesjekkMetadata;
import no.nav.fo.veilarb.dokument.domain.ArenaOppfolgingssak;
import no.nav.fo.veilarb.dokument.domain.OppfolgingsenhetDto;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.client.Client;

import java.util.Optional;

import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;
import static no.nav.apiapp.util.UrlUtils.clusterUrlForApplication;
import static no.nav.apiapp.util.UrlUtils.joinPaths;
import static no.nav.fo.veilarb.dokument.config.ApplicationConfig.VEILARBARENA_API_URL_PROPERTY;
import static no.nav.fo.veilarb.dokument.util.AuthUtils.createBearerToken;
import static no.nav.sbl.util.EnvironmentUtils.getOptionalProperty;

@Slf4j
@Component
public class ArenaClient implements Helsesjekk {

    private final Client restClient;
    private final String host;

    @Inject
    public ArenaClient(Client restClient) {
        this.restClient = restClient;
        host = getOptionalProperty(VEILARBARENA_API_URL_PROPERTY)
                .orElseGet(() ->
                        joinPaths(clusterUrlForApplication("veilarbarena"), "/veilarbarena/api"));
    }

    public String oppfolgingsenhet(String fnr) {
        return restClient
                .target(joinPaths(host, "oppfolgingsbruker", fnr))
                .request()
                .header(AUTHORIZATION, createBearerToken())
                .get(OppfolgingsenhetDto.class)
                .getNavKontor();
    }

    public Optional<ArenaOppfolgingssak> oppfolgingssak(String fnr) {
        ArenaOppfolgingssak oppfolgingssak = restClient
                .target(joinPaths(host, "oppfolgingssak", fnr))
                .request()
                .header(AUTHORIZATION, createBearerToken())
                .get(ArenaOppfolgingssak.class);

        return Optional.ofNullable(oppfolgingssak);
    }

    @Override
    public void helsesjekk() {
        int status = restClient.target(joinPaths(clusterUrlForApplication("veilarbarena"), "/veilarbarena/internal/isAlive")).request().get().getStatus();

        if (status != 200) {
            throw new IllegalStateException("Rest kall mot veilarbarena feilet");
        }
    }

    @Override
    public HelsesjekkMetadata getMetadata() {
        return new HelsesjekkMetadata(
                "veilarbarena helsesjekk",
                joinPaths(clusterUrlForApplication("veilarbarena"), "/veilarbarena/internal/isAlive"),
                "veilarbarena - isAlive",
                false
        );
    }
}
