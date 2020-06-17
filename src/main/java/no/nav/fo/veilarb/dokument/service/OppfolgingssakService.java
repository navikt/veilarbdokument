package no.nav.fo.veilarb.dokument.service;

import no.nav.fo.veilarb.dokument.client.api.ArenaClient;
import no.nav.fo.veilarb.dokument.client.api.SakClient;
import no.nav.fo.veilarb.dokument.domain.ArenaOppfolgingssak;
import no.nav.fo.veilarb.dokument.domain.Bruker;
import no.nav.fo.veilarb.dokument.domain.Sak;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OppfolgingssakService {

    private final SakClient sakClient;
    private final ArenaClient arenaClient;
    private final MetrikkService metrikkService;

    OppfolgingssakService(SakClient sakClient, ArenaClient arenaClient, MetrikkService metrikkService) {
        this.sakClient = sakClient;
        this.arenaClient = arenaClient;
        this.metrikkService = metrikkService;
    }

    public Sak hentOppfolgingssak(Bruker bruker) {
        List<Sak> oppfolgingssaker = sakClient.hentOppfolgingssaker(bruker.getAktorId());

        if (oppfolgingssaker.size() == 1) {
            metrikkService.rapporterSak("funnet");
            return oppfolgingssaker.get(0);
        } else if (oppfolgingssaker.size() == 0) {
            String fagsakNr = getArenaOppfolgingssak(bruker).getOppfolgingssakId();
            return opprettOppfolgingssak(bruker, fagsakNr);
        } else {
            String fagsakNr = getArenaOppfolgingssak(bruker).getOppfolgingssakId();
            return oppfolgingssaker.stream()
                    .filter(sak -> sak.fagsakNr().equals(fagsakNr))
                    .findFirst()
                    .orElseGet(() -> opprettOppfolgingssak(bruker, fagsakNr));
        }
    }

    private Sak opprettOppfolgingssak(Bruker bruker, String fagsakNr) {
        metrikkService.rapporterSak("opprettet");
        return sakClient.opprettOppfolgingssak(bruker.getAktorId(), fagsakNr);
    }

    private ArenaOppfolgingssak getArenaOppfolgingssak(Bruker bruker) {
        return arenaClient.oppfolgingssak(bruker.getFnr())
                .orElseThrow(() -> new IllegalStateException(
                        String.format("Fant ikke oppfolgingssak i Arena for bruker med aktør id %s", bruker.getAktorId())));
    }
}
