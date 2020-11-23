package no.nav.fo.veilarb.dokument.localtestapp.stub;

import no.nav.common.types.identer.EnhetId;
import no.nav.fo.veilarb.dokument.client.api.EnhetClient;
import no.nav.common.client.norg2.Enhet;
import no.nav.fo.veilarb.dokument.domain.EnhetKontaktinformasjon;
import no.nav.fo.veilarb.dokument.domain.EnhetOrganisering;

import java.util.List;

import static java.util.Arrays.asList;
import static no.nav.fo.veilarb.dokument.localtestapp.stub.Values.*;

public class EnhetClientStub implements EnhetClient {
    @Override
    public List<Enhet> hentAktiveEnheter() {
        return asList(new Enhet().setNavn(TEST_ENHET_NAVN).setEnhetNr(TEST_ENHET.get()));
    }

    @Override
    public EnhetKontaktinformasjon hentKontaktinfo(EnhetId enhetId) {
        return new EnhetKontaktinformasjon(TEST_ENHET, TEST_ENHET_ADRESSE);
    }

    @Override
    public List<EnhetOrganisering> hentEnhetOrganisering(EnhetId enhetId) {
        return null;
    }
}
