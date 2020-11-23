package no.nav.fo.veilarb.dokument.client.api;

import no.nav.common.client.norg2.Enhet;
import no.nav.common.health.HealthCheck;
import no.nav.common.types.identer.EnhetId;
import no.nav.fo.veilarb.dokument.domain.EnhetKontaktinformasjon;
import no.nav.fo.veilarb.dokument.domain.EnhetOrganisering;

import java.util.List;

public interface EnhetClient extends HealthCheck {

    List<Enhet> hentAktiveEnheter();

    EnhetKontaktinformasjon hentKontaktinfo(EnhetId enhetId);

    List<EnhetOrganisering> hentEnhetOrganisering(EnhetId enhetId);

}
