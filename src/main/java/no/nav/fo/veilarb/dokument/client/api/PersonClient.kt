package no.nav.fo.veilarb.dokument.client.api

import no.nav.common.types.identer.EksternBrukerId
import no.nav.fo.veilarb.dokument.domain.Adresse
import no.nav.fo.veilarb.dokument.domain.Målform

interface PersonClient {
    fun hentPerson(id: EksternBrukerId): Person

    data class Person(val navn: String,
                      val adresse: Adresse,
                      val malform: Målform)
}
