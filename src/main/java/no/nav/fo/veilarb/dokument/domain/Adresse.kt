package no.nav.fo.veilarb.dokument.domain

import no.nav.fo.veilarb.dokument.client.api.BrevClient
import java.lang.IllegalStateException

data class Adresse(
        val adresselinje1: String,
        val adresselinje2: String? = null,
        val adresselinje3: String? = null,
        val postnummer: String,
        val poststed: String,
) {
    companion object {
        fun fraEnhetPostadresse(enhetPostadresse: EnhetPostadresse): BrevClient.Returadresse {
            when (enhetPostadresse) {
                is EnhetPostboksadresse ->
                    return BrevClient.Returadresse(
                            adresselinje =
                            "Postboks ${enhetPostadresse.postboksnummer ?: ""} ${enhetPostadresse.postboksanlegg ?: ""}",
                            postnummer = enhetPostadresse.postnummer,
                            poststed = enhetPostadresse.poststed,
                    )

                is EnhetStedsadresse ->
                    return BrevClient.Returadresse(
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
