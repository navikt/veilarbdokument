package no.nav.fo.veilarb.dokument.localtestapp.stub;

import no.nav.common.health.HealthCheckResult;
import no.nav.fo.veilarb.dokument.client.api.SakClient;
import no.nav.fo.veilarb.dokument.domain.Sak;

import java.util.Collections;
import java.util.List;

import static no.nav.fo.veilarb.dokument.localtestapp.stub.Values.TEST_SAK_ID;

public class SakClientStub implements SakClient {
    @Override
    public List<Sak> hentOppfolgingssaker(String aktorId) {
        return Collections.emptyList();
    }

    @Override
    public Sak opprettOppfolgingssak(String aktorId, String fagsakNr) {
        return new Sak(TEST_SAK_ID, null, null, fagsakNr, null);
    }

    @Override
    public HealthCheckResult checkHealth() {
        return HealthCheckResult.healthy();
    }
}
