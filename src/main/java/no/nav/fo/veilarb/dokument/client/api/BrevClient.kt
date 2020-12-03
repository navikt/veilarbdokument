package no.nav.fo.veilarb.dokument.client.api

import no.nav.common.health.HealthCheck
import no.nav.fo.veilarb.dokument.domain.*

interface BrevClient: HealthCheck {
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
            val utkast: Boolean
    )

    data class Mottaker(val navn: String)

    data class Returadresse(
            val adresselinje: String,
            val postnummer: String,
            val poststed: String,
    ) {
        companion object {
            fun fraEnhetPostadresse(enhetPostadresse: EnhetPostadresse): Returadresse {
                when (enhetPostadresse) {
                    is EnhetPostboksadresse ->
                        return Returadresse(
                                adresselinje =
                                "Postboks ${enhetPostadresse.postboksnummer ?: ""} ${enhetPostadresse.postboksanlegg ?: ""}",
                                postnummer = enhetPostadresse.postnummer,
                                poststed = enhetPostadresse.poststed,
                        )

                    is EnhetStedsadresse ->
                        return Returadresse(
                                adresselinje =
                                "${enhetPostadresse.gatenavn ?: ""} ${enhetPostadresse.husnummer ?: ""}${enhetPostadresse.husbokstav ?: ""}",
                                postnummer = enhetPostadresse.postnummer,
                                poststed = enhetPostadresse.poststed,
                        )
                    else -> throw IllegalStateException("Manglende mapping for enhetPostadresse")
                }
            }
        }

    }
}

