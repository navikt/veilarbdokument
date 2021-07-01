package no.nav.fo.veilarb.dokument.client.impl

import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.junit.WireMockRule
import no.nav.common.rest.client.RestClient
import no.nav.common.types.identer.Fnr
import no.nav.fo.veilarb.dokument.client.api.BrevClient
import no.nav.fo.veilarb.dokument.domain.MalType
import no.nav.fo.veilarb.dokument.domain.Målform
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class BrevClientImplTest {

    lateinit var brevClient: BrevClient

    private val wireMockRule = WireMockRule()

    @Rule
    fun getWireMockRule() = wireMockRule

    @Before
    fun setup() {
        val client = RestClient.baseClient()
        brevClient = BrevClientImpl(client, "http://localhost:" + getWireMockRule().port())
    }

    @Test
    fun request_til_brev_client_har_forventet_innhold() {

        val brevdata = BrevClient.Brevdata(
            malType = MalType.SITUASJONSBESTEMT_INNSATS_SKAFFE_ARBEID,
            veilederNavn = "Veileder Navn",
            navKontor = "Nav kontor",
            kontaktEnhetNavn = "Nav kontor kontakt",
            kontaktTelefonnummer = "00000000",
            dato = "20. januar 2020",
            malform = Målform.NB,
            begrunnelse = listOf("Avsnitt 1", "Avsnitt 2"),
            kilder = listOf("Kilde 1", "Kilde 2"),
            mottaker = BrevClient.Mottaker(
                fnr = Fnr("12345678901"),
                navn = "Mottaker Navn"
            ),
            postadresse = BrevClient.Adresse(
                adresselinje = "Retur adresselinje",
                postnummer = "4321",
                poststed = "Retur poststed"
            ),
            utkast = false
        )

        val documentResponse = "document"

        val forventetInnhold =
            """
                    {
                      "malType": "SITUASJONSBESTEMT_INNSATS_SKAFFE_ARBEID",
                      "veilederNavn": "Veileder Navn",
                      "navKontor": "Nav kontor",
                      "kontaktTelefonnummer": "00000000",
                      "kontaktEnhetNavn": "Nav kontor kontakt",
                      "dato": "20. januar 2020",
                      "malform": "NB",
                      "begrunnelse": ["Avsnitt 1", "Avsnitt 2"],
                      "kilder": ["Kilde 1", "Kilde 2"],
                      "mottaker": {
                        "fnr": "12345678901",
                        "navn": "Mottaker Navn"
                      },
                      "postadresse": {
                        "adresselinje": "Retur adresselinje",
                        "postnummer": "4321",
                        "poststed": "Retur poststed"
                      },
                      "utkast": false
                    }
                """


        WireMock.givenThat(
            WireMock.post(WireMock.urlEqualTo("/api/v1/genpdf/vedtak14a/vedtak14a"))
                .withRequestBody(WireMock.equalToJson(forventetInnhold))
                .willReturn(
                    WireMock.aResponse()
                        .withStatus(200)
                        .withBody(documentResponse.toByteArray())
                        .withHeader("Content-Type", "application/pdf")
                )
        )


        val response = brevClient.genererBrev(brevdata)

        assertEquals(documentResponse, response.decodeToString())
    }
}

