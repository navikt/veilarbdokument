package no.nav.fo.veilarb.dokument.client.api;

import no.nav.common.health.HealthCheck;

public interface VeilederClient extends HealthCheck {

    String hentVeiledernavn();

}
