package no.nav.fo.veilarb.dokument.client.api

import no.nav.common.health.HealthCheck
import no.nav.common.types.identer.Fnr
import no.nav.fo.veilarb.dokument.domain.Målform

interface PersonClient: HealthCheck {
    fun hentPerson(fnr: Fnr): Person

    data class Person(val navn: String,
                      val malform: Målform?)
}
