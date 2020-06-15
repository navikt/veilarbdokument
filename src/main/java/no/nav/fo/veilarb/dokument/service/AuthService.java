package no.nav.fo.veilarb.dokument.service;

import no.nav.common.abac.Pep;
import no.nav.common.abac.domain.AbacPersonId;
import no.nav.common.abac.domain.request.ActionId;
import no.nav.common.auth.subject.IdentType;
import no.nav.common.auth.subject.Subject;
import no.nav.common.auth.subject.SubjectHandler;
import no.nav.common.client.aktorregister.AktorregisterClient;
import no.nav.fo.veilarb.dokument.client.ArenaClient;
import no.nav.fo.veilarb.dokument.domain.Bruker;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
public class AuthService {

    private final AktorregisterClient aktorregisterClient;
    private final Pep pep;
    private final ArenaClient arenaClient;

    public AuthService(AktorregisterClient aktorregisterClient,
                       Pep pep,
                       ArenaClient arenaClient) {
        this.aktorregisterClient = aktorregisterClient;
        this.pep = pep;
        this.arenaClient = arenaClient;
    }


    public Bruker sjekkTilgang(String fnr, String veilederEnhet) {
        sjekkInternBruker();

        String aktorId = aktorregisterClient.hentAktorId(fnr);

        sjekkTilgangTilPerson(aktorId);
        sjekkRiktigEnhet(fnr, veilederEnhet);
        sjekkTilgangTilEnhet(veilederEnhet);

        return new Bruker(fnr, aktorId);
    }

    private void sjekkTilgangTilPerson(String aktorId) {
        boolean harTilgang = pep.harVeilederTilgangTilPerson(getInnloggetVeilederIdent(), ActionId.WRITE, AbacPersonId.aktorId(aktorId));
        if (!harTilgang) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    private void sjekkRiktigEnhet(String fnr, String veilederEnhet) {
        String oppfolgingsenhet = arenaClient.oppfolgingsenhet(fnr);

        if (!veilederEnhet.equals(oppfolgingsenhet)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Feil enhet");
        }
    }

    private void sjekkTilgangTilEnhet(String enhet) {
        if (!pep.harVeilederTilgangTilEnhet(getInnloggetVeilederIdent(), enhet)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Ikke tilgang til enhet");
        }
    }

    private void sjekkInternBruker() {
        SubjectHandler
                .getIdentType()
                .filter(IdentType.InternBruker::equals)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Ikke intern bruker"));
    }

    public String getInnloggetVeilederIdent() {
        return SubjectHandler
                .getSubject()
                .filter(subject -> IdentType.InternBruker.equals(subject.getIdentType()))
                .map(Subject::getUid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Fant ikke ident for innlogget veileder"));
    }
}
