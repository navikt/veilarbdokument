package no.nav.fo.veilarb.dokument.service

import no.nav.fo.veilarb.dokument.client.api.BrevClient
import no.nav.fo.veilarb.dokument.client.api.BrevClient.Returadresse.Companion.fraEnhetPostadresse
import no.nav.fo.veilarb.dokument.client.api.PersonClient
import no.nav.fo.veilarb.dokument.client.api.VeilederClient
import no.nav.fo.veilarb.dokument.domain.Adresse
import no.nav.fo.veilarb.dokument.domain.DokumentbestillingDto
import no.nav.fo.veilarb.dokument.domain.EnhetKontaktinformasjon
import no.nav.fo.veilarb.dokument.util.DateUtils
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class DokumentV2Service(val brevClient: BrevClient,
                        val personClient: PersonClient,
                        val veilederClient: VeilederClient,
                        val kontaktEnhetService: KontaktEnhetService) {

    fun lagDokumentutkast(dokumentBestilling: DokumentbestillingDto): ByteArray {

        val brevdata = lagBrevdata(dokumentBestilling)

        return brevClient.genererBrev(brevdata)
    }

    private fun lagBrevdata(dokumentBestilling: DokumentbestillingDto): BrevClient.Brevdata {
        val enhetKontaktinformasjon: EnhetKontaktinformasjon = kontaktEnhetService.utledEnhetKontaktinformasjon(dokumentBestilling.enhetId)

        val person = personClient.hentPerson(dokumentBestilling.brukerFnr)

        val veilederNavn = veilederClient.hentVeiledernavn()

        val enhetNavn = veilederClient.hentEnhetNavn(dokumentBestilling.enhetId)
        val kontaktEnhetNavn =
                if (dokumentBestilling.enhetId == enhetKontaktinformasjon.enhetNr) enhetNavn
                else veilederClient.hentEnhetNavn(enhetKontaktinformasjon.enhetNr)

        // TODO: Avklare innhold i brev før dette implementeres. Tomt innhold for testing.
        val todoAdresse = Adresse(adresselinje1 = "", postnummer = "", poststed = "")
        val mottaker = BrevClient.Mottaker(person.navn, todoAdresse)
        val dato = LocalDate.now().format(DateUtils.norskDateFormatter)

        val returadresse = fraEnhetPostadresse(enhetKontaktinformasjon.postadresse)

        val newLineAscii = "\u21b5"
        val begrunnelseAvsnitt =
                dokumentBestilling.begrunnelse?.split(newLineAscii)?.filterNot { it.isEmpty() } ?: emptyList()

        return BrevClient.Brevdata(
                malType = dokumentBestilling.malType,
                veilederNavn = veilederNavn,
                navKontor = enhetNavn,
                kontaktEnhetNavn = kontaktEnhetNavn,
                dato = dato,
                malform = person.malform,
                mottaker = mottaker,
                returadresse = returadresse,
                begrunnelse = begrunnelseAvsnitt,
                kilder = dokumentBestilling.opplysninger
        )
    }

}
