package no.nav.fo.veilarb.dokument.localtestapp.stub;

import no.nav.common.health.HealthCheckResult;
import no.nav.fo.veilarb.dokument.client.api.VeilederClient;

public class VeilederClientStub implements VeilederClient {

    @Override
    public String hentVeiledernavn() {
        return Values.TEST_VEILEDER_NAVN;
    }

    @Override
    public HealthCheckResult checkHealth() {
        return HealthCheckResult.healthy();
    }
}
