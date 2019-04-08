package no.nav.fo.veilarb.dokument.service;

import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import no.nav.fo.veilarb.dokument.domain.Sak;
import no.nav.sbl.util.EnvironmentUtils;
import no.nav.sbl.util.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static no.nav.fo.veilarb.dokument.ApplicationConfig.SAK_API_URL;

@Component
public class SakService {
    private Client restClient;
    private String host;

    @Inject
    public SakService(Client restClient) {
        this.restClient = restClient;
        this.host = EnvironmentUtils.getRequiredProperty(SAK_API_URL);
    }

    public List<Sak> finnSaker(String aktorId, String tema) {
        Response response = restClient.target(host)
                .queryParam("aktoerId", aktorId)
                .queryParam("tema", tema)
                .request()
                .get();

        return response.readEntity(new GenericType<List<Sak>>() {
        });
    }

    public static Sak finnGjeldendeOppfolgingssak(List<Sak> saker) {
        return saker.stream().filter(sak ->
                Objects.equals(sak.tema(), "OPP") &&
                        StringUtils.notNullOrEmpty(sak.fagsakNr()) &&
                        Objects.equals(sak.applikasjon(), "AO01"))
                .max(Comparator.comparing(Sak::opprettetTidspunkt))
                .orElseThrow(() -> new IllegalStateException("Mangler oppføgningssak"));

    }
}
