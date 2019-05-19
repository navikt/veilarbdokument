package no.nav.fo.veilarb.dokument.service;

import no.nav.fo.veilarb.dokument.domain.OpprettSakDto;
import no.nav.fo.veilarb.dokument.domain.Sak;
import no.nav.sbl.util.EnvironmentUtils;
import no.nav.sbl.util.StringUtils;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
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

    public List<Sak> finnOppfolgingssaker(String aktorId) {

        Response response = restClient
                .target(host)
                .queryParam("aktoerId", aktorId)
                .queryParam("tema", OPPFOLGING_KODE)
                .request()
                .get();

        List<Sak> saker = response.readEntity(new GenericType<List<Sak>>() {
        });

        return filtrerOppfolgingssaker(saker);
    }

    public Sak opprettOppfolgingssak(String aktorId, String fagsakNr) {
        OpprettSakDto entity = new OpprettSakDto(OPPFOLGING_KODE, ARENA_KODE, aktorId, fagsakNr);

        return restClient
                .target(host)
                .request()
                .post(Entity.json(entity))
                .readEntity(Sak.class); // TODO antagelse at vi får Sak i response
    }

    private static List<Sak> filtrerOppfolgingssaker(List<Sak> saker) {

        return saker.stream().filter(sak ->
                Objects.equals(sak.tema(), OPPFOLGING_KODE) &&
                        StringUtils.notNullOrEmpty(sak.fagsakNr()) &&
                        Objects.equals(sak.applikasjon(), ARENA_KODE))
                .collect(Collectors.toList());

    }
}
