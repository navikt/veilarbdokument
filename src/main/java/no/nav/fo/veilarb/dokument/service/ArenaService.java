package no.nav.fo.veilarb.dokument.service;

import lombok.extern.slf4j.Slf4j;
import no.nav.apiapp.selftest.Helsesjekk;
import no.nav.apiapp.selftest.HelsesjekkMetadata;
import no.nav.fo.veilarb.dokument.domain.ArenaOppfolgingssak;
import no.nav.fo.veilarb.dokument.domain.OppfolgingsenhetDto;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.ws.rs.client.Client;

import java.util.Optional;

import static no.nav.apiapp.util.UrlUtils.joinPaths;
import static no.nav.fo.veilarb.dokument.ApplicationConfig.VEILARBARENA_API_URL_PROPERTY;
import static no.nav.sbl.util.EnvironmentUtils.getRequiredProperty;

@Slf4j
@Service
public class ArenaService implements Helsesjekk {

    private Client restClient;
    private String host;

    @Inject
    public ArenaService(Client restClient) {
        this.restClient = restClient;
        host = getRequiredProperty(VEILARBARENA_API_URL_PROPERTY);
    }
    public String oppfolgingsenhet(String fnr) {
        return restClient
                .target(joinPaths(host, "oppfolgingsbruker", fnr))
                .request()
                .get(OppfolgingsenhetDto.class)
                .getNavKontor();
    }

    public Optional<ArenaOppfolgingssak> oppfolgingssak(String fnr) {
        ArenaOppfolgingssak oppfolgingssak = restClient
                .target(joinPaths(host, "oppfolgingssak", fnr))
                .request()
                .get(ArenaOppfolgingssak.class);

        return Optional.ofNullable(oppfolgingssak);
    }

    @Override
    public void helsesjekk() {
        int status = restClient.target(host).path("ping").request().get().getStatus();

        if (status != 200) {
            throw new IllegalStateException("Rest kall mot veilarbarena feilet");
        }
    }

    @Override
    public HelsesjekkMetadata getMetadata() {
        return new HelsesjekkMetadata(
                "veilarbarena helsesjekk",
                host,
                "veilarbarena - ping",
                true
        );
    }
}
