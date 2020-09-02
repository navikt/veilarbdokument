package no.nav.fo.veilarb.dokument.client.api;

import no.nav.fo.veilarb.dokument.domain.EnhetKontaktinformasjon;
import no.nav.fo.veilarb.dokument.domain.EnhetOrganisering;

import java.util.List;

public interface EnhetClient {

    EnhetKontaktinformasjon hentKontaktinfo(String enhetId);

    List<EnhetOrganisering> hentEnhetOrganisering(String enhetId);

}
