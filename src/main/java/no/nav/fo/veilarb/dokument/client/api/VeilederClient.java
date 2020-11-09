package no.nav.fo.veilarb.dokument.client.api;

import no.nav.common.health.HealthCheck;
import no.nav.common.types.identer.EnhetId;

public interface VeilederClient extends HealthCheck {

    String hentVeiledernavn();

    String hentEnhetNavn(EnhetId enhetId);

}
