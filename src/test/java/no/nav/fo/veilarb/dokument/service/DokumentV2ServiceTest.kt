package no.nav.fo.veilarb.dokument.service

import no.nav.common.client.norg2.Enhet
import no.nav.common.types.identer.EnhetId
import no.nav.common.types.identer.Fnr
import no.nav.fo.veilarb.dokument.client.api.BrevClient
import no.nav.fo.veilarb.dokument.client.api.PersonClient
import no.nav.fo.veilarb.dokument.client.api.VeilederClient
import no.nav.fo.veilarb.dokument.domain.*
import no.nav.fo.veilarb.dokument.util.DateUtils
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import java.time.LocalDate

class DokumentV2ServiceTest {

    val brevClient: BrevClient = mock(BrevClient::class.java)
    val personClient: PersonClient = mock(PersonClient::class.java)
    val veilederClient: VeilederClient = mock(VeilederClient::class.java)
    val enhetInfoService: EnhetInfoService = mock(EnhetInfoService::class.java)

    val service: DokumentV2Service =
        DokumentV2Service(
            brevClient,
            personClient,
            veilederClient,
            enhetInfoService
        )

    val dto = ProduserDokumentV2DTO(
        brukerFnr = Fnr("123"),
        navn = "Navn Navnesen",
        malType = MalType.STANDARD_INNSATS_BEHOLDE_ARBEID,
        enhetId = EnhetId("000"),
        begrunnelse = "begrunnelse",
        opplysninger = listOf("Kilde1", "kilde2"),
        utkast = false,
        adresse = ProduserDokumentV2DTO.AdresseDTO(
            adresselinje1 = "Adresselinje 1",
            adresselinje2 = "Adresselinje 2",
            adresselinje3 = "Adresselinje 3",
            postnummer = "0000",
            poststed = "Sted",
            land = "Sverige"
        )
    )
    val enhetPostadresse = EnhetPostboksadresse("", "", "", "")
    val telefonnummer = "00000000"
    val enhetNavn = "Navn Enhet"
    val kontaktEnhetNavn = "Navn Kontaktenhet"
    val kontaktEnhetId = EnhetId("001")
    val enhetKontaktinformasjon = EnhetKontaktinformasjon(kontaktEnhetId, enhetPostadresse, telefonnummer)
    val veilederNavn = "Navn Veileder"
    val personMålform = Målform.NB

    val forventetBrev = "brev".toByteArray()
    val forventetAdresse = BrevClient.Adresse(
        adresselinje = "Postboks ${enhetPostadresse.postboksnummer ?: ""} ${enhetPostadresse.postboksanlegg ?: ""}",
        postnummer = enhetPostadresse.postnummer,
        poststed = enhetPostadresse.poststed
    )
    val forventetBrevdata = BrevClient.Brevdata(
        malType = dto.malType,
        veilederNavn = veilederNavn,
        navKontor = enhetNavn,
        kontaktEnhetNavn = kontaktEnhetNavn,
        kontaktTelefonnummer = telefonnummer,
        dato = LocalDate.now().format(DateUtils.norskDateFormatter),
        malform = personMålform,
        begrunnelse = listOf(dto.begrunnelse!!),
        kilder = dto.opplysninger,
        mottaker = BrevClient.Mottaker(
            navn = dto.navn,
            adresselinje1 = dto.adresse.adresselinje1,
            adresselinje2 = dto.adresse.adresselinje2,
            adresselinje3 = dto.adresse.adresselinje3,
            postnummer = dto.adresse.postnummer,
            poststed = dto.adresse.poststed,
            land = dto.adresse.land
        ),
        postadresse = forventetAdresse,
        utkast = dto.utkast
    )


    @Before
    fun setup() {
        `when`(enhetInfoService.utledEnhetKontaktinformasjon(dto.enhetId)).thenReturn(enhetKontaktinformasjon)
        `when`(veilederClient.hentVeiledernavn()).thenReturn(veilederNavn)
        `when`(enhetInfoService.hentEnhet(dto.enhetId)).thenReturn(
            Enhet().setEnhetNr(dto.enhetId.get()).setNavn(enhetNavn)
        )
        `when`(enhetInfoService.hentEnhet(kontaktEnhetId)).thenReturn(
            Enhet().setEnhetNr(kontaktEnhetId.get()).setNavn(kontaktEnhetNavn)
        )
        `when`(personClient.hentMålform(dto.brukerFnr)).thenReturn(personMålform)
        `when`(brevClient.genererBrev(forventetBrevdata)).thenReturn("brev".toByteArray())
    }

    @Test
    fun `produserDokument genererer brev`() {
        val produserDokument = service.produserDokument(dto)

        assertEquals(String(forventetBrev), String(produserDokument))
    }

    @Test
    fun `produserDokument feiler dersom navn for enhet mangler`() {
        `when`(enhetInfoService.hentEnhet(dto.enhetId)).thenReturn(Enhet().setEnhetNr(dto.enhetId.get()).setNavn(null))
        val exception = Assertions.assertThrows(IllegalStateException::class.java) {
            service.produserDokument(dto)
        }

        assertEquals("Manglende navn for enhet ${dto.enhetId}", exception.message)
    }

    @Test
    fun `produserDokument feiler dersom navn for kontaktenhet mangler`() {
        `when`(enhetInfoService.hentEnhet(kontaktEnhetId)).thenReturn(
            Enhet().setEnhetNr(kontaktEnhetId.get()).setNavn(null)
        )
        val exception = Assertions.assertThrows(IllegalStateException::class.java) {
            service.produserDokument(dto)
        }

        assertEquals("Manglende navn for enhet ${kontaktEnhetId}", exception.message)
    }

    @Test
    fun `produserDokument feiler dersom telefonnummer for kontaktenhet mangler`() {
        `when`(enhetInfoService.utledEnhetKontaktinformasjon(dto.enhetId))
            .thenReturn(EnhetKontaktinformasjon(kontaktEnhetId, enhetPostadresse, null))
        val exception = Assertions.assertThrows(IllegalStateException::class.java) {
            service.produserDokument(dto)
        }

        assertEquals("Manglende telefonnummer for enhet ${kontaktEnhetId}", exception.message)
    }

    private fun <T> any(): T = Mockito.any()
}
