package no.nav.fo.veilarb.dokument.client.api

import no.nav.common.health.HealthCheck
import no.nav.common.types.identer.Fnr
import no.nav.fo.veilarb.dokument.domain.*

interface BrevClient: HealthCheck {
    fun genererBrev(brevdata: Brevdata): ByteArray

    data class Brevdata(
        val malType: MalType,
        val veilederNavn: String,
        val navKontor: String,
        val kontaktEnhetNavn: String,
        val kontaktTelefonnummer: String,
        val dato: String,
        val malform: Målform,
        val begrunnelse: List<String>,
        val kilder: List<String>,
        val mottaker: Mottaker,
        val postadresse: Adresse,
        val utkast: Boolean
    )

    data class Mottaker(
        val fnr: Fnr,
        val navn: String
    )

    data class Adresse(
            val adresselinje: String,
            val postnummer: String,
            val poststed: String,
    ) {
        companion object {
            fun fraEnhetPostadresse(enhetPostadresse: EnhetPostadresse): Adresse {
                when (enhetPostadresse) {
                    is EnhetPostboksadresse ->
                        return Adresse(
                                adresselinje =
                                "Postboks ${enhetPostadresse.postboksnummer ?: ""} ${enhetPostadresse.postboksanlegg ?: ""}",
                                postnummer = enhetPostadresse.postnummer,
                                poststed = enhetPostadresse.poststed
                        )

                    is EnhetStedsadresse ->
                        return Adresse(
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

