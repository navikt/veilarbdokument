package no.nav.fo.veilarb.dokument.localtestapp.stub;

import no.nav.common.types.identer.EnhetId;
import no.nav.fo.veilarb.dokument.client.api.EnhetClient;
import no.nav.fo.veilarb.dokument.domain.EnhetKontaktinformasjon;
import no.nav.fo.veilarb.dokument.domain.EnhetOrganisering;

import java.util.List;

import static no.nav.fo.veilarb.dokument.localtestapp.stub.Values.TEST_ENHET;
import static no.nav.fo.veilarb.dokument.localtestapp.stub.Values.TEST_ENHET_ADRESSE;

public class EnhetClientStub implements EnhetClient {
    @Override
    public EnhetKontaktinformasjon hentKontaktinfo(EnhetId enhetId) {
        return new EnhetKontaktinformasjon(TEST_ENHET, TEST_ENHET_ADRESSE);
    }

    @Override
    public List<EnhetOrganisering> hentEnhetOrganisering(EnhetId enhetId) {
        return null;
    }
}
