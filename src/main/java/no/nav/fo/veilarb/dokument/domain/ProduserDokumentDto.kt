package no.nav.fo.veilarb.dokument.domain

import no.nav.common.types.identer.EnhetId
import no.nav.common.types.identer.Fnr

data class ProduserDokumentDto(
        val brukerFnr: Fnr,
        val malType: MalType,
        val enhetId: EnhetId,
        val begrunnelse: String?,
        val opplysninger: List<String>,
        val utkast: Boolean
)
