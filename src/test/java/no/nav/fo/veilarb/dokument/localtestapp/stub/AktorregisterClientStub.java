package no.nav.fo.veilarb.dokument.localtestapp.stub;

import no.nav.common.client.aktorregister.AktorregisterClient;
import no.nav.common.client.aktorregister.IdentOppslag;
import no.nav.common.health.HealthCheckResult;
import no.nav.common.types.identer.AktorId;
import no.nav.common.types.identer.Fnr;

import java.util.List;

import static no.nav.fo.veilarb.dokument.localtestapp.stub.Values.TEST_AKTOR_ID;

public class AktorregisterClientStub implements AktorregisterClient {
    @Override
    public Fnr hentFnr(AktorId s) {
        return null;
    }

    @Override
    public AktorId hentAktorId(Fnr s) {
        return TEST_AKTOR_ID;
    }

    @Override
    public List<IdentOppslag> hentFnr(List<AktorId> list) {
        return null;
    }

    @Override
    public List<IdentOppslag> hentAktorId(List<Fnr> list) {
        return null;
    }

    @Override
    public HealthCheckResult checkHealth() {
        return HealthCheckResult.healthy();
    }

    @Override
    public List<AktorId> hentAktorIder(Fnr fnr) {
        return null;
    }
}
