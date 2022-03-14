package no.nav.fo.veilarb.dokument.localtestapp.stub

import no.nav.common.health.HealthCheckResult
import no.nav.common.types.identer.Fnr
import no.nav.fo.veilarb.dokument.client.api.PersonClient
import no.nav.fo.veilarb.dokument.domain.Målform
import no.nav.fo.veilarb.dokument.localtestapp.stub.Values.*

class PersonClientStub : PersonClient {
    override fun hentMålform(fnr: Fnr): Målform {
        return TEST_MÅLFORM
    }

    override fun checkHealth(): HealthCheckResult {
        return HealthCheckResult.healthy()
    }
}
