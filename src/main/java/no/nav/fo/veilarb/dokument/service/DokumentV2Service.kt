package no.nav.fo.veilarb.dokument.service

import no.nav.fo.veilarb.dokument.client.api.BrevClient
import no.nav.fo.veilarb.dokument.client.api.PersonClient
import no.nav.fo.veilarb.dokument.client.api.VeilederClient
import no.nav.fo.veilarb.dokument.domain.Adresse
import no.nav.fo.veilarb.dokument.domain.DokumentbestillingDto
import no.nav.fo.veilarb.dokument.domain.EnhetKontaktinformasjon
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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

        val hentPerson = personClient.hentPerson(dokumentBestilling.brukerFnr)

        val enhetNavn = veilederClient.hentEnhetNavn(dokumentBestilling.enhetId)
        val kontaktEnhetNavn =
                if (dokumentBestilling.enhetId == enhetKontaktinformasjon.enhetNr) enhetNavn
                else veilederClient.hentEnhetNavn(enhetKontaktinformasjon.enhetNr)

        val mottaker = BrevClient.Mottaker(hentPerson.navn, hentPerson.adresse)
        val dato = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)

        val returadresse = Adresse.fraEnhetPostadresse(enhetKontaktinformasjon.postadresse)

        return BrevClient.Brevdata(
                malType = dokumentBestilling.malType,
                navKontor = enhetNavn,
                kontaktEnhetNavn = kontaktEnhetNavn,
                dato = dato,
                malform = hentPerson.malform,
                mottaker = mottaker,
                returadresse = returadresse
        )
    }

}
