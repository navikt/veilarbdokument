package no.nav.fo.veilarb.dokument.service;

import lombok.extern.slf4j.Slf4j;
import no.nav.fo.veilarb.dokument.domain.OppfolgingsenhetDto;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.ws.rs.client.Client;

import static no.nav.apiapp.util.UrlUtils.joinPaths;
import static no.nav.fo.veilarb.dokument.ApplicationConfig.VEILARBARENA_API_URL_PROPERTY;
import static no.nav.sbl.util.EnvironmentUtils.getRequiredProperty;

@Slf4j
@Service
public class ArenaService {

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

}
