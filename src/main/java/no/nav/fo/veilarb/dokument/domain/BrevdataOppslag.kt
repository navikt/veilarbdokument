package no.nav.fo.veilarb.dokument.domain

import no.nav.fo.veilarb.dokument.client.api.PersonClient

data class BrevdataOppslag(val enhetKontaktinformasjon: EnhetKontaktinformasjon,
                           val person: PersonClient.Person,
                           val veilederNavn: String,
                           val enhetNavn: String,
                           val kontaktEnhetNavn: String)
