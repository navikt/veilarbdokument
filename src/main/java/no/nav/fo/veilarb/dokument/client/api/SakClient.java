package no.nav.fo.veilarb.dokument.client.api;

import no.nav.common.health.HealthCheck;
import no.nav.fo.veilarb.dokument.domain.Sak;

import java.util.List;

public interface SakClient extends HealthCheck {

    List<Sak> hentOppfolgingssaker(String aktorId);

    Sak opprettOppfolgingssak(String aktorId, String fagsakNr);

}
