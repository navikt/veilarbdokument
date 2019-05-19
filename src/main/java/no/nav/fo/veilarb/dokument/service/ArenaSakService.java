package no.nav.fo.veilarb.dokument.service;

import no.nav.virksomhet.gjennomforing.sak.arbeidogaktivitet.v1.Sak;
import no.nav.virksomhet.tjenester.sak.arbeidogaktivitet.v1.ArbeidOgAktivitet;
import no.nav.virksomhet.tjenester.sak.meldinger.v1.WSBruker;
import no.nav.virksomhet.tjenester.sak.meldinger.v1.WSHentSakListeRequest;
import no.nav.virksomhet.tjenester.sak.meldinger.v1.WSHentSakListeResponse;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Optional;

import static no.nav.fo.veilarb.dokument.service.SakService.OPPFOLGING_KODE;

@Service
public class ArenaSakService {

    private ArbeidOgAktivitet arbeidOgAktivitet;

    @Inject
    ArenaSakService(ArbeidOgAktivitet arbeidOgAktivitet) {
        this.arbeidOgAktivitet = arbeidOgAktivitet;
    }

    public Optional<Sak> hentOppfolgingssakFraArena(String fnr) {

        WSHentSakListeRequest request = new WSHentSakListeRequest()
                .withBruker(new WSBruker().withBrukertypeKode("PERSON").withBruker(fnr))
                .withFagomradeKode(OPPFOLGING_KODE);

        WSHentSakListeResponse wsHentSakListeResponse = arbeidOgAktivitet.hentSakListe(request);

        return wsHentSakListeResponse.getSakListe().stream().findFirst();
    }
}
