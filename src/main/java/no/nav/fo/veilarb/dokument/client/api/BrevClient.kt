package no.nav.fo.veilarb.dokument.client.api

import no.nav.fo.veilarb.dokument.domain.Adresse
import no.nav.fo.veilarb.dokument.domain.MalType
import no.nav.fo.veilarb.dokument.domain.Målform

interface BrevClient {
    fun genererBrev(brevdata: Brevdata): ByteArray

    data class Brevdata(
            val malType: MalType,
            val veilederNavn: String,
            val navKontor: String,
            val kontaktEnhetNavn: String,
            val dato: String,
            val malform: Målform,
            val begrunnelse: List<String>,
            val kilder: List<String>,
            val mottaker: Mottaker,
            val returadresse: Returadresse,
    )

    data class Mottaker(val navn: String,
                        val adresse: Adresse)

    data class Returadresse(
            val adresselinje: String,
            val postnummer: String,
            val poststed: String,
    )
}

