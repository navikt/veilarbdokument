package no.nav.fo.veilarb.dokument.service

import no.nav.common.client.norg2.Enhet
import no.nav.common.types.identer.EnhetId
import no.nav.common.types.identer.Fnr
import no.nav.fo.veilarb.dokument.client.api.BrevClient
import no.nav.fo.veilarb.dokument.client.api.BrevClient.Adresse.Companion.fraEnhetPostadresse
import no.nav.fo.veilarb.dokument.client.api.PersonClient
import no.nav.fo.veilarb.dokument.client.api.VeilederClient
import no.nav.fo.veilarb.dokument.domain.BrevdataOppslagV2
import no.nav.fo.veilarb.dokument.domain.EnhetKontaktinformasjon
import no.nav.fo.veilarb.dokument.domain.ProduserDokumentV2DTO
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

    fun produserDokument(dto: ProduserDokumentV2DTO): ByteArray {

        val brevdataOppslag = hentBrevdata(dto.brukerFnr, dto.enhetId)
        val brevdata = mapBrevdata(dto, brevdataOppslag)

        return brevClient.genererBrev(brevdata)
    }

    private fun hentBrevdata(fnr: Fnr, enhetId: EnhetId): BrevdataOppslagV2 {
        val enhetKontaktinformasjon: EnhetKontaktinformasjon = enhetInfoService.utledEnhetKontaktinformasjon(enhetId)
        val målform = personClient.hentMålform(fnr)
        val veilederNavn: String = veilederClient.hentVeiledernavn()

        val enhetNavn: Enhet = enhetInfoService.hentEnhet(enhetId)
        val kontaktEnhetNavn: Enhet = enhetInfoService.hentEnhet(enhetKontaktinformasjon.enhetNr)

        return BrevdataOppslagV2(
            enhetKontaktinformasjon = enhetKontaktinformasjon,
            målform = målform,
            veilederNavn = veilederNavn,
            enhet = enhetNavn,
            kontaktEnhet = kontaktEnhetNavn
        )
    }

    companion object {

        fun mapBrevdata(dto: ProduserDokumentV2DTO, brevdataOppslag: BrevdataOppslagV2): BrevClient.Brevdata {

            val mottaker = BrevClient.Mottaker(
                        navn = dto.navn,
                        adresselinje1 = dto.adresse.adresselinje1,
                        adresselinje2 = dto.adresse.adresselinje2,
                        adresselinje3 = dto.adresse.adresselinje3,
                        postnummer = dto.adresse.postnummer,
                        poststed = dto.adresse.poststed,
                        land = dto.adresse.land
            )
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

            return BrevClient.Brevdata(
                malType = dto.malType,
                veilederNavn = brevdataOppslag.veilederNavn,
                navKontor = enhetNavn,
                kontaktEnhetNavn = kontaktEnhetNavn,
                kontaktTelefonnummer = telefonnummer,
                dato = dato,
                malform = brevdataOppslag.målform,
                mottaker = mottaker,
                postadresse = postadresse,
                begrunnelse = begrunnelseAvsnitt,
                kilder = dto.opplysninger,
                utkast = dto.utkast
            )
        }
    }
}
