package no.nav.fo.veilarb.dokument.service;

import no.nav.common.abac.Pep;
import no.nav.common.abac.domain.request.ActionId;
import no.nav.common.auth.context.AuthContextHolder;
import no.nav.common.client.aktorregister.AktorregisterClient;
import no.nav.common.types.identer.AktorId;
import no.nav.common.types.identer.EnhetId;
import no.nav.common.types.identer.Fnr;
import no.nav.common.types.identer.NavIdent;
import no.nav.fo.veilarb.dokument.client.api.ArenaClient;
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


    public Bruker sjekkTilgang(Fnr fnr, EnhetId veilederEnhet) {
        sjekkInternBruker();

        AktorId aktorId = aktorregisterClient.hentAktorId(fnr);

        sjekkTilgangTilPerson(aktorId);
        sjekkRiktigEnhet(fnr, veilederEnhet);
        sjekkTilgangTilEnhet(veilederEnhet);

        return new Bruker(fnr, aktorId);
    }

    private void sjekkTilgangTilPerson(AktorId aktorId) {
        boolean harTilgang = pep.harVeilederTilgangTilPerson(getInnloggetVeilederIdent(), ActionId.WRITE, aktorId);
        if (!harTilgang) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    private void sjekkRiktigEnhet(Fnr fnr, EnhetId veilederEnhet) {
        EnhetId oppfolgingsenhet = arenaClient.oppfolgingsenhet(fnr);

        if (!veilederEnhet.equals(oppfolgingsenhet)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Feil enhet");
        }
    }

    private void sjekkTilgangTilEnhet(EnhetId enhet) {
        if (!pep.harVeilederTilgangTilEnhet(getInnloggetVeilederIdent(), enhet)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Ikke tilgang til enhet");
        }
    }

    private void sjekkInternBruker() {
        if (!AuthContextHolder.erInternBruker()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Ikke intern bruker");
        }
    }

    public NavIdent getInnloggetVeilederIdent() {
        return AuthContextHolder.getNavIdent().orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.BAD_REQUEST, "Fant ikke ident for innlogget veileder"));
    }
}
