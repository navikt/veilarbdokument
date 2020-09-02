package no.nav.fo.veilarb.dokument.localtestapp.stub;

import no.nav.common.client.aktorregister.AktorregisterClient;
import no.nav.common.client.aktorregister.IdentOppslag;
import no.nav.common.health.HealthCheckResult;

import java.util.List;

import static no.nav.fo.veilarb.dokument.localtestapp.stub.Values.TEST_AKTOR_ID;

public class AktorregisterClientStub implements AktorregisterClient {
    @Override
    public String hentFnr(String s) {
        return null;
    }

    @Override
    public String hentAktorId(String s) {
        return TEST_AKTOR_ID;
    }

    @Override
    public List<IdentOppslag> hentFnr(List<String> list) {
        return null;
    }

    @Override
    public List<IdentOppslag> hentAktorId(List<String> list) {
        return null;
    }

    @Override
    public HealthCheckResult checkHealth() {
        return HealthCheckResult.healthy();
    }
}
