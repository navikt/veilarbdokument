package no.nav.fo.veilarb.dokument.service;

import lombok.extern.slf4j.Slf4j;
import no.nav.fo.veilarb.dokument.domain.VeilederDto;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.ws.rs.client.Client;

import static no.nav.apiapp.util.UrlUtils.joinPaths;
import static no.nav.fo.veilarb.dokument.ApplicationConfig.VEILARBVEILEDER_API_URL_PROPERTY;
import static no.nav.sbl.util.EnvironmentUtils.getRequiredProperty;

@Slf4j
@Service
public class VeilederService {

    private Client restClient;
    private String host;

    @Inject
    public VeilederService(Client restClient) {
        this.restClient = restClient;
        host = getRequiredProperty(VEILARBVEILEDER_API_URL_PROPERTY);
    }
    public String veiledernavn() {
        return restClient
                .target(joinPaths(host, "veileder", "me"))
                .request()
                .get(VeilederDto.class)
                .navn();
    }
}
