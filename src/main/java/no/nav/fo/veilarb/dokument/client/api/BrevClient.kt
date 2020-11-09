package no.nav.fo.veilarb.dokument.client.api

import no.nav.fo.veilarb.dokument.domain.Adresse
import no.nav.fo.veilarb.dokument.domain.MalType
import no.nav.fo.veilarb.dokument.domain.Målform

interface BrevClient {
    fun genererBrev(brevdataPdf: Brevdata): ByteArray

    data class Brevdata(
            val malType: MalType,
            val navKontor: String,
            val kontaktEnhetNavn: String,
            val dato: String,
            val malform: Målform,
            val mottaker: Mottaker,
            val returadresse: Adresse
    )

    data class Mottaker(val navn: String,
                        val adresse: Adresse)

}

