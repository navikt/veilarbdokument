package no.nav.fo.veilarb.dokument.service

import no.nav.common.types.identer.EnhetId
import no.nav.common.types.identer.Fnr
import no.nav.fo.veilarb.dokument.client.api.BrevClient
import no.nav.fo.veilarb.dokument.client.api.BrevClient.Returadresse.Companion.fraEnhetPostadresse
import no.nav.fo.veilarb.dokument.client.api.PersonClient
import no.nav.fo.veilarb.dokument.client.api.VeilederClient
import no.nav.fo.veilarb.dokument.domain.Adresse
import no.nav.fo.veilarb.dokument.domain.BrevdataOppslag
import no.nav.fo.veilarb.dokument.domain.DokumentbestillingDto
import no.nav.fo.veilarb.dokument.domain.EnhetKontaktinformasjon
import no.nav.fo.veilarb.dokument.util.DateUtils
import no.nav.fo.veilarb.dokument.util.StringUtils.splitNewline
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class DokumentV2Service(val brevClient: BrevClient,
                        val personClient: PersonClient,
                        val veilederClient: VeilederClient,
                        val kontaktEnhetService: KontaktEnhetService) {

    fun lagDokumentutkast(dokumentBestilling: DokumentbestillingDto): ByteArray {

        val brevdataOppslag = hentBrevdata(dokumentBestilling.brukerFnr, dokumentBestilling.enhetId)
        val brevdata = mapBrevdata(dokumentBestilling, brevdataOppslag)

        return brevClient.genererBrev(brevdata)
    }

    private fun hentBrevdata(fnr: Fnr, enhetId: EnhetId): BrevdataOppslag {
        val enhetKontaktinformasjon: EnhetKontaktinformasjon = kontaktEnhetService.utledEnhetKontaktinformasjon(enhetId)
        val person: PersonClient.Person = personClient.hentPerson(fnr)
        val veilederNavn: String = veilederClient.hentVeiledernavn()
        val enhetNavn: String = veilederClient.hentEnhetNavn(enhetId)
        val kontaktEnhetNavn: String = veilederClient.hentEnhetNavn(enhetKontaktinformasjon.enhetNr)

        return BrevdataOppslag(
                enhetKontaktinformasjon = enhetKontaktinformasjon,
                person = person,
                veilederNavn = veilederNavn,
                enhetNavn = enhetNavn,
                kontaktEnhetNavn = kontaktEnhetNavn
        )
    }

    companion object {

        fun mapBrevdata(dokumentBestilling: DokumentbestillingDto, brevdataOppslag: BrevdataOppslag): BrevClient.Brevdata {

            // TODO: Avklare innhold i brev før dette implementeres. Tomt innhold for testing.
            val todoAdresse = Adresse(adresselinje1 = "", postnummer = "", poststed = "")
            val mottaker = BrevClient.Mottaker(brevdataOppslag.person.navn, todoAdresse)
            val dato = LocalDate.now().format(DateUtils.norskDateFormatter)
            val returadresse = fraEnhetPostadresse(brevdataOppslag.enhetKontaktinformasjon.postadresse)

            val begrunnelseAvsnitt =
                    dokumentBestilling.begrunnelse?.let { splitNewline(it) }?.filterNot { it.isEmpty() } ?: emptyList()

            return BrevClient.Brevdata(
                    malType = dokumentBestilling.malType,
                    veilederNavn = brevdataOppslag.veilederNavn,
                    navKontor = brevdataOppslag.enhetNavn,
                    kontaktEnhetNavn = brevdataOppslag.kontaktEnhetNavn,
                    dato = dato,
                    malform = brevdataOppslag.person.malform,
                    mottaker = mottaker,
                    returadresse = returadresse,
                    begrunnelse = begrunnelseAvsnitt,
                    kilder = dokumentBestilling.opplysninger
            )
        }
    }
}
