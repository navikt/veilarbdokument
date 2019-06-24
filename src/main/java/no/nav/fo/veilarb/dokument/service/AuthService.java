package no.nav.fo.veilarb.dokument.service;


import no.nav.apiapp.feil.IngenTilgang;
import no.nav.apiapp.security.veilarbabac.Bruker;
import no.nav.apiapp.security.veilarbabac.VeilarbAbacPepClient;
import no.nav.common.auth.SubjectHandler;
import no.nav.dialogarena.aktor.AktorService;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

import static no.nav.brukerdialog.security.domain.IdentType.InternBruker;

@Service
public class AuthService {

    private AktorService aktorService;
    private VeilarbAbacPepClient pepClient;
    private ArenaService arenaService;

    @Inject
    public AuthService(AktorService aktorService,
                       VeilarbAbacPepClient pepClient,
                       ArenaService arenaService) {
        this.aktorService = aktorService;
        this.pepClient = pepClient;
        this.arenaService = arenaService;
    }


    public Bruker sjekkTilgang(String fnr, String veilederEnhet) {
        sjekkInternBruker();

        String aktorId = getAktorIdOrThrow(fnr);
        Bruker bruker = Bruker.fraAktoerId(aktorId).medFoedselsnummer(fnr);

        pepClient.sjekkSkrivetilgangTilBruker(bruker);
        sjekkRiktigEnhet(fnr, veilederEnhet);
        sjekkTilgangTilEnhet(veilederEnhet);

        return bruker;
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