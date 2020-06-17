package no.nav.fo.veilarb.dokument.client.api;

import no.nav.common.health.HealthCheck;
import no.nav.fo.veilarb.dokument.domain.ArenaOppfolgingssak;

import java.util.Optional;

public interface ArenaClient extends HealthCheck {

    String oppfolgingsenhet(String fnr);

    Optional<ArenaOppfolgingssak> oppfolgingssak(String fnr);
}
