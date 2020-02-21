package no.nav.fo.veilarb.dokument.service;

import no.nav.apiapp.feil.IngenTilgang;
import no.nav.apiapp.security.PepClient;
import no.nav.common.auth.SubjectHandler;
import no.nav.dialogarena.aktor.AktorService;
import no.nav.fo.veilarb.dokument.domain.Bruker;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

import static no.nav.brukerdialog.security.domain.IdentType.InternBruker;

@Service
public class AuthService {

    private AktorService aktorService;
    private PepClient pepClient;
    private ArenaService arenaService;

    @Inject
    public AuthService(AktorService aktorService,
                       PepClient pepClient,
                       ArenaService arenaService) {
        this.aktorService = aktorService;
        this.pepClient = pepClient;
        this.arenaService = arenaService;
    }


    public Bruker sjekkTilgang(String fnr, String veilederEnhet) {
        sjekkInternBruker();

        String aktorId = getAktorIdOrThrow(fnr);

        pepClient.sjekkSkrivetilgangTilAktorId(aktorId);
        sjekkRiktigEnhet(fnr, veilederEnhet);
        sjekkTilgangTilEnhet(veilederEnhet);

        return new Bruker(fnr, aktorId);
    }

    private void sjekkRiktigEnhet(String fnr, String veilederEnhet) {
        String oppfolgingsenhet = arenaService.oppfolgingsenhet(fnr);

        if (!veilederEnhet.equals(oppfolgingsenhet)) {
            throw new IngenTilgang("Feil enhet");
        }
    }

    private void sjekkTilgangTilEnhet(String enhet) {
        if(!pepClient.harTilgangTilEnhet(enhet)) {
            throw new IngenTilgang("Ikke tilgang til enhet");
        }
    }

    private void sjekkInternBruker() {
        SubjectHandler
                .getIdentType()
                .filter(InternBruker::equals)
                .orElseThrow(() -> new IngenTilgang("Ikke intern bruker"));
    }

    private String getAktorIdOrThrow(String fnr) {
        return aktorService.getAktorId(fnr)
                .orElseThrow(() -> new IllegalArgumentException("Fant ikke aktør for fnr"));
    }
}
