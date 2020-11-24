package no.nav.fo.veilarb.dokument.localtestapp.stub

import no.nav.common.health.HealthCheckResult
import no.nav.common.types.identer.Fnr
import no.nav.fo.veilarb.dokument.client.api.PersonClient
import no.nav.fo.veilarb.dokument.localtestapp.stub.Values.*

class PersonClientStub : PersonClient {
    override fun hentPerson(fnr: Fnr): PersonClient.Person {
        return PersonClient.Person(
                navn = TEST_PERSON_NAVN,
                malform = TEST_MÅLFORM,
        )
    }

    override fun checkHealth(): HealthCheckResult {
        return HealthCheckResult.healthy()
    }
}
