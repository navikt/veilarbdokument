package no.nav.fo.veilarb.dokument.localtestapp.stub

import no.nav.common.health.HealthCheckResult
import no.nav.fo.veilarb.dokument.client.api.BrevClient

class BrevClientStub : BrevClient {
    override fun genererBrev(brevdata: BrevClient.Brevdata): ByteArray {
         return ByteArray(1)
    }

    override fun checkHealth(): HealthCheckResult {
        return HealthCheckResult.healthy()
    }
}
