package no.nav.fo.veilarb.dokument.domain

import no.nav.common.client.norg2.Enhet

data class BrevdataOppslagV2(val enhetKontaktinformasjon: EnhetKontaktinformasjon,
                             val målform: Målform,
                             val veilederNavn: String,
                             val enhet: Enhet,
                             val kontaktEnhet: Enhet)
