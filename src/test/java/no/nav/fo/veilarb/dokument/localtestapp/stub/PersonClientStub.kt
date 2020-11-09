package no.nav.fo.veilarb.dokument.localtestapp.stub

import no.nav.common.types.identer.EksternBrukerId
import no.nav.fo.veilarb.dokument.client.api.PersonClient
import no.nav.fo.veilarb.dokument.localtestapp.stub.Values.*

class PersonClientStub : PersonClient {
    override fun hentPerson(id: EksternBrukerId): PersonClient.Person {
        return PersonClient.Person(
                navn = TEST_PERSON_NAVN,
                adresse = TEST_ADRESSE,
                malform = TEST_MÅLFORM,
        )
    }
}
