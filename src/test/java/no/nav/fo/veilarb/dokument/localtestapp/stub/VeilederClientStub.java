package no.nav.fo.veilarb.dokument.localtestapp.stub;

import no.nav.common.health.HealthCheckResult;
import no.nav.fo.veilarb.dokument.client.api.VeilederClient;

public class VeilederClientStub implements VeilederClient {
    @Override
    public String hentVeiledernavn() {
        return null;
    }

    @Override
    public HealthCheckResult checkHealth() {
        return HealthCheckResult.healthy();
    }
}
