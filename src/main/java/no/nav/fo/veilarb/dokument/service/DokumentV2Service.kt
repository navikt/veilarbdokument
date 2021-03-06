package no.nav.fo.veilarb.dokument.service

import no.nav.common.types.identer.EnhetId
import no.nav.common.types.identer.Fnr
import no.nav.fo.veilarb.dokument.client.api.BrevClient
import no.nav.fo.veilarb.dokument.client.api.BrevClient.Returadresse.Companion.fraEnhetPostadresse
import no.nav.fo.veilarb.dokument.client.api.PersonClient
import no.nav.fo.veilarb.dokument.client.api.VeilederClient
import no.nav.fo.veilarb.dokument.domain.BrevdataOppslag
import no.nav.fo.veilarb.dokument.domain.EnhetKontaktinformasjon
import no.nav.fo.veilarb.dokument.domain.ProduserDokumentDto
import no.nav.fo.veilarb.dokument.util.DateUtils
import no.nav.fo.veilarb.dokument.util.StringUtils.splitNewline
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class DokumentV2Service(val brevClient: BrevClient,
                        val personClient: PersonClient,
                        val veilederClient: VeilederClient,
                        val enhetInfoService: EnhetInfoService) {

    fun produserDokument(dto: ProduserDokumentDto): ByteArray {

        val brevdataOppslag = hentBrevdata(dto.brukerFnr, dto.enhetId)
        val brevdata = mapBrevdata(dto, brevdataOppslag)

        return brevClient.genererBrev(brevdata)
    }

    private fun hentBrevdata(fnr: Fnr, enhetId: EnhetId): BrevdataOppslag {
        val enhetKontaktinformasjon: EnhetKontaktinformasjon = enhetInfoService.utledEnhetKontaktinformasjon(enhetId)
        val person: PersonClient.Person = personClient.hentPerson(fnr)
        val veilederNavn: String = veilederClient.hentVeiledernavn()

        val enhetNavn: String = enhetInfoService.hentEnhet(enhetId).navn
        val kontaktEnhetNavn: String = enhetInfoService.hentEnhet(enhetKontaktinformasjon.enhetNr).navn

        return BrevdataOppslag(
                enhetKontaktinformasjon = enhetKontaktinformasjon,
                person = person,
                veilederNavn = veilederNavn,
                enhetNavn = enhetNavn,
                kontaktEnhetNavn = kontaktEnhetNavn
        )
    }

    companion object {

        fun mapBrevdata(dto: ProduserDokumentDto, brevdataOppslag: BrevdataOppslag): BrevClient.Brevdata {

            val mottaker = BrevClient.Mottaker(brevdataOppslag.person.navn)
            val dato = LocalDate.now().format(DateUtils.norskDateFormatter)
            val returadresse = fraEnhetPostadresse(brevdataOppslag.enhetKontaktinformasjon.postadresse)

            val begrunnelseAvsnitt =
                    dto.begrunnelse?.let { splitNewline(it) }?.filterNot { it.isEmpty() } ?: emptyList()

            return BrevClient.Brevdata(
                    malType = dto.malType,
                    veilederNavn = brevdataOppslag.veilederNavn,
                    navKontor = brevdataOppslag.enhetNavn,
                    kontaktEnhetNavn = brevdataOppslag.kontaktEnhetNavn,
                    dato = dato,
                    malform = brevdataOppslag.person.malform,
                    mottaker = mottaker,
                    returadresse = returadresse,
                    begrunnelse = begrunnelseAvsnitt,
                    kilder = dto.opplysninger,
                    utkast = dto.utkast
            )
        }
    }
}
