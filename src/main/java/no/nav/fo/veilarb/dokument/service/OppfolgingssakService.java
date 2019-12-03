package no.nav.fo.veilarb.dokument.service;

import no.nav.apiapp.security.veilarbabac.Bruker;
import no.nav.fo.veilarb.dokument.domain.ArenaOppfolgingssak;
import no.nav.fo.veilarb.dokument.domain.Sak;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Service
public class OppfolgingssakService {

    private SakService sakService;
    private ArenaService arenaService;

    @Inject
    OppfolgingssakService(SakService sakService, ArenaService arenaService) {
        this.sakService = sakService;
        this.arenaService = arenaService;
    }

    public Sak hentOppfolgingssak(Bruker bruker) {
        List<Sak> oppfolgingssaker = sakService.finnOppfolgingssaker(bruker.getAktoerId());

        if (oppfolgingssaker.size() == 1) {
            return oppfolgingssaker.get(0);
        } else if (oppfolgingssaker.size() == 0) {
            String fagsakNr = getArenaOppfolgingssak(bruker).getOppfolgingssakId();
            return sakService.opprettOppfolgingssak(bruker.getAktoerId(), fagsakNr);
        } else {
            String fagsakNr = getArenaOppfolgingssak(bruker).getOppfolgingssakId();
            return oppfolgingssaker.stream()
                    .filter(sak -> sak.fagsakNr().equals(fagsakNr))
                    .findFirst()
                    .orElseGet(() -> sakService.opprettOppfolgingssak(bruker.getAktoerId(), fagsakNr));
        }
    }

    private ArenaOppfolgingssak getArenaOppfolgingssak(Bruker bruker) {
        return arenaService.oppfolgingssak(bruker.getFoedselsnummer())
                .orElseThrow(() -> new IllegalStateException(
                        String.format("Fant ikke oppfolgingssak i Arena for bruker med aktør id %s", bruker.getAktoerId())));
    }
}
