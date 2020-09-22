package no.nav.fo.veilarb.dokument.client.api;

import no.nav.common.types.identer.EnhetId;
import no.nav.fo.veilarb.dokument.domain.EnhetKontaktinformasjon;
import no.nav.fo.veilarb.dokument.domain.EnhetOrganisering;

import java.util.List;

public interface EnhetClient {

    EnhetKontaktinformasjon hentKontaktinfo(EnhetId enhetId);

    List<EnhetOrganisering> hentEnhetOrganisering(EnhetId enhetId);

}
