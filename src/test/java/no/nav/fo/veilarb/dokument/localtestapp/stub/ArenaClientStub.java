package no.nav.fo.veilarb.dokument.localtestapp.stub;

import no.nav.common.health.HealthCheckResult;
import no.nav.common.types.identer.EnhetId;
import no.nav.common.types.identer.Fnr;
import no.nav.fo.veilarb.dokument.client.api.ArenaClient;
import no.nav.fo.veilarb.dokument.domain.ArenaOppfolgingssak;

import java.util.Optional;

import static no.nav.fo.veilarb.dokument.localtestapp.stub.Values.TEST_ARENA_OPPFOLGINGSSAK;
import static no.nav.fo.veilarb.dokument.localtestapp.stub.Values.TEST_ENHET;

public class ArenaClientStub implements ArenaClient {
    @Override
    public EnhetId oppfolgingsenhet(Fnr fnr) {
        return TEST_ENHET;
    }

    @Override
    public Optional<ArenaOppfolgingssak> oppfolgingssak(Fnr fnr) {
        return Optional.of(new ArenaOppfolgingssak(TEST_ARENA_OPPFOLGINGSSAK));
    }

    @Override
    public HealthCheckResult checkHealth() {
        return HealthCheckResult.healthy();
    }
}
