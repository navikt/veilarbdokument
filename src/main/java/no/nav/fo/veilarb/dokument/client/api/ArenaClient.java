package no.nav.fo.veilarb.dokument.client.api;

import no.nav.common.health.HealthCheck;
import no.nav.common.types.identer.EnhetId;
import no.nav.common.types.identer.Fnr;
import no.nav.fo.veilarb.dokument.domain.ArenaOppfolgingssak;

import java.util.Optional;

public interface ArenaClient extends HealthCheck {

    EnhetId oppfolgingsenhet(Fnr fnr);

    Optional<ArenaOppfolgingssak> oppfolgingssak(Fnr fnr);
}
