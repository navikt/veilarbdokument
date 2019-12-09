package no.nav.fo.veilarb.dokument.service;

import lombok.extern.slf4j.Slf4j;
import no.nav.apiapp.selftest.Helsesjekk;
import no.nav.apiapp.selftest.HelsesjekkMetadata;
import no.nav.fo.veilarb.dokument.domain.VeilederDto;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.ws.rs.client.Client;

import static no.nav.apiapp.util.UrlUtils.clusterUrlForApplication;
import static no.nav.apiapp.util.UrlUtils.joinPaths;
import static no.nav.fo.veilarb.dokument.ApplicationConfig.VEILARBVEILEDER_API_URL_PROPERTY;
import static no.nav.sbl.util.EnvironmentUtils.getOptionalProperty;

@Slf4j
@Service
public class VeilederService implements Helsesjekk {

    private Client restClient;
    private String host;

    @Inject
    public VeilederService(Client restClient) {
        this.restClient = restClient;
        host = getOptionalProperty(VEILARBVEILEDER_API_URL_PROPERTY)
                .orElseGet(() ->
                        joinPaths(clusterUrlForApplication("veilarbveileder"), "/veilarbveileder/api"));
    }

    public String veiledernavn() {
        return restClient
                .target(joinPaths(host, "veileder", "me"))
                .request()
                .get(VeilederDto.class)
                .navn();
    }

    @Override
    public void helsesjekk() {
        int status = restClient.target(host).path("ping").request().get().getStatus();

        if (status != 200) {
            throw new IllegalStateException("Rest kall mot veilarbveileder feilet");
        }
    }

    @Override
    public HelsesjekkMetadata getMetadata() {
        return new HelsesjekkMetadata(
                "veilarbveileder helsesjekk",
                host,
                "veilarbveileder - ping",
                true
        );
    }
}
