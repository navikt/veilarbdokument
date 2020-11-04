package no.nav.fo.veilarb.dokument.localtestapp.stub;

import no.nav.common.types.identer.EnhetId;
import no.nav.fo.veilarb.dokument.client.api.EnhetClient;
import no.nav.fo.veilarb.dokument.domain.EnhetKontaktinformasjon;
import no.nav.fo.veilarb.dokument.domain.EnhetOrganisering;
import no.nav.fo.veilarb.dokument.domain.EnhetPostboksadresse;

import java.util.List;

import static no.nav.fo.veilarb.dokument.localtestapp.stub.Values.TEST_ENHET;

public class EnhetClientStub implements EnhetClient {
    @Override
    public EnhetKontaktinformasjon hentKontaktinfo(EnhetId enhetId) {
        return new EnhetKontaktinformasjon(TEST_ENHET, new EnhetPostboksadresse(null, null,null, null));
    }

    @Override
    public List<EnhetOrganisering> hentEnhetOrganisering(EnhetId enhetId) {
        return null;
    }
}
