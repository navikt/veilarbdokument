package no.nav.fo.veilarb.dokument.service;

import no.nav.fo.veilarb.dokument.domain.Sak;
import no.nav.sbl.util.EnvironmentUtils;
import no.nav.sbl.util.StringUtils;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static no.nav.fo.veilarb.dokument.ApplicationConfig.SAK_API_URL;

@Component
public class SakService {

    private Client restClient;
    private String host;

    public static String ARENA_KODE = "AO01";
    public static String OPPFOLGING_KODE = "OPP";

    @Inject
    public SakService(Client restClient) {
        this.restClient = restClient;
        this.host = EnvironmentUtils.getRequiredProperty(SAK_API_URL);
    }

    public Sak finnGjeldendeOppfolgingssak(String aktorId) {

        List<Sak> saker = finnSaker(aktorId, OPPFOLGING_KODE);
        List<Sak> oppfolgingssaker = filtrerOppfolgingssaker(saker);

        if (oppfolgingssaker.size() == 1) {
            return oppfolgingssaker.get(0);
        } else if (oppfolgingssaker.isEmpty()) {
            throw new IllegalStateException("Mangler oppføgningssak");
        } else {
            throw new IllegalStateException("Flere oppføgningssaker");
        }
    }

    private List<Sak> finnSaker(String aktorId, String tema) {

        Response response = restClient
                .target(host)
                .queryParam("aktoerId", aktorId)
                .queryParam("tema", tema)
                .request()
                .get();

        return response.readEntity(new GenericType<List<Sak>>() {
        });
    }

    private static List<Sak> filtrerOppfolgingssaker(List<Sak> saker) {

        return saker.stream().filter(sak ->
                Objects.equals(sak.tema(), OPPFOLGING_KODE) &&
                        StringUtils.notNullOrEmpty(sak.fagsakNr()) &&
                        Objects.equals(sak.applikasjon(), ARENA_KODE))
                .collect(Collectors.toList());

    }
}
