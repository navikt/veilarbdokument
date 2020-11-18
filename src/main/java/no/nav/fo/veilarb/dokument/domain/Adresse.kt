package no.nav.fo.veilarb.dokument.domain

data class Adresse(
        val adresselinje1: String,
        val adresselinje2: String? = null,
        val adresselinje3: String? = null,
        val postnummer: String,
        val poststed: String,
)
