package no.nav.fo.veilarb.dokument.domain

import java.lang.IllegalStateException

data class Adresse(
        val adresselinje1: String,
        val adresselinje2: String? = null,
        val adresselinje3: String? = null,
        val postnummer: String,
        val poststed: String,
) {
    companion object {
        fun fraEnhetPostadresse(enhetPostadresse: EnhetPostadresse): Adresse {
            when (enhetPostadresse) {
                is EnhetPostboksadresse ->
                    return Adresse(
                            adresselinje1 =
                            "Postboks ${enhetPostadresse.postboksnummer ?: ""} ${enhetPostadresse.postboksanlegg ?: ""}",
                            postnummer = enhetPostadresse.postnummer,
                            poststed = enhetPostadresse.poststed,
                    )

                is EnhetStedsadresse ->
                    return Adresse(
                            adresselinje1 =
                            "${enhetPostadresse.gatenavn ?: ""} ${enhetPostadresse.husnummer ?: ""}${enhetPostadresse.husbokstav ?: ""}",
                            postnummer = enhetPostadresse.postnummer,
                            poststed = enhetPostadresse.poststed,
                    )
                else -> throw IllegalStateException("Manglende mapping for enhetPostadresse")
            }
        }
    }
}
