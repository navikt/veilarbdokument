package no.nav.fo.veilarb.dokument.service

import no.nav.common.client.norg2.Enhet
import no.nav.common.types.identer.EnhetId
import no.nav.common.types.identer.Fnr
import no.nav.fo.veilarb.dokument.client.api.BrevClient
import no.nav.fo.veilarb.dokument.client.api.BrevClient.Adresse.Companion.fraEnhetPostadresse
import no.nav.fo.veilarb.dokument.client.api.PersonClient
import no.nav.fo.veilarb.dokument.client.api.VeilederClient
import no.nav.fo.veilarb.dokument.domain.BrevdataOppslag
import no.nav.fo.veilarb.dokument.domain.EnhetKontaktinformasjon
import no.nav.fo.veilarb.dokument.domain.Målform
import no.nav.fo.veilarb.dokument.domain.ProduserDokumentDto
import no.nav.fo.veilarb.dokument.util.DateUtils
import no.nav.fo.veilarb.dokument.util.StringUtils.splitNewline
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class DokumentV2Service(
    val brevClient: BrevClient,
    val personClient: PersonClient,
    val veilederClient: VeilederClient,
    val enhetInfoService: EnhetInfoService
) {

    fun produserDokument(dto: ProduserDokumentDto): ByteArray {

        val brevdataOppslag = hentBrevdata(dto.brukerFnr, dto.enhetId)
        val brevdata = mapBrevdata(dto, brevdataOppslag)

        return brevClient.genererBrev(brevdata)
    }

    private fun hentBrevdata(fnr: Fnr, enhetId: EnhetId): BrevdataOppslag {
        val enhetKontaktinformasjon: EnhetKontaktinformasjon = enhetInfoService.utledEnhetKontaktinformasjon(enhetId)
        val person: PersonClient.Person = personClient.hentPerson(fnr)
        val veilederNavn: String = veilederClient.hentVeiledernavn()

        val enhetNavn: Enhet = enhetInfoService.hentEnhet(enhetId)
        val kontaktEnhetNavn: Enhet = enhetInfoService.hentEnhet(enhetKontaktinformasjon.enhetNr)

        return BrevdataOppslag(
            enhetKontaktinformasjon = enhetKontaktinformasjon,
            person = person,
            veilederNavn = veilederNavn,
            enhet = enhetNavn,
            kontaktEnhet = kontaktEnhetNavn
        )
    }

    companion object {

        fun mapBrevdata(dto: ProduserDokumentDto, brevdataOppslag: BrevdataOppslag): BrevClient.Brevdata {

            val mottaker = BrevClient.Mottaker(fnr = dto.brukerFnr, navn = brevdataOppslag.person.navn)
            val dato = LocalDate.now().format(DateUtils.norskDateFormatter)
            val postadresse = fraEnhetPostadresse(brevdataOppslag.enhetKontaktinformasjon.postadresse)


            val enhetNavn = brevdataOppslag.enhet.navn ?: throw IllegalStateException(
                "Manglende navn for enhet ${brevdataOppslag.enhet.enhetNr}"
            )

            val kontaktEnhetNavn = brevdataOppslag.kontaktEnhet.navn ?: throw IllegalStateException(
                "Manglende navn for enhet ${brevdataOppslag.kontaktEnhet.enhetNr}"
            )

            val telefonnummer = brevdataOppslag.enhetKontaktinformasjon.telefonnummer ?: throw IllegalStateException(
                "Manglende telefonnummer for enhet ${brevdataOppslag.enhetKontaktinformasjon.enhetNr}"
            )

            val begrunnelseAvsnitt =
                dto.begrunnelse?.let { splitNewline(it) }?.filterNot { it.isEmpty() } ?: emptyList()

            val malform = brevdataOppslag.person.malform ?: Målform.NB

            return BrevClient.Brevdata(
                malType = dto.malType,
                veilederNavn = brevdataOppslag.veilederNavn,
                navKontor = enhetNavn,
                kontaktEnhetNavn = kontaktEnhetNavn,
                kontaktTelefonnummer = telefonnummer,
                dato = dato,
                malform = malform,
                mottaker = mottaker,
                postadresse = postadresse,
                begrunnelse = begrunnelseAvsnitt,
                kilder = dto.opplysninger,
                utkast = dto.utkast
            )
        }
    }
}
