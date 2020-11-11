package no.nav.fo.veilarb.dokument.localtestapp.stub;

import no.nav.common.health.HealthCheckResult;
import no.nav.common.types.identer.EnhetId;
import no.nav.fo.veilarb.dokument.client.api.VeilederClient;

import static no.nav.fo.veilarb.dokument.localtestapp.stub.Values.TEST_ENHET_NAVN;

public class VeilederClientStub implements VeilederClient {

    @Override
    public String hentVeiledernavn() {
        return Values.TEST_VEILEDER_NAVN;
    }

    @Override
    public String hentEnhetNavn(EnhetId enhetId) {
        return TEST_ENHET_NAVN;
    }

    @Override
    public HealthCheckResult checkHealth() {
        return HealthCheckResult.healthy();
    }
}
