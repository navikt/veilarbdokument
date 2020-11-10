package no.nav.fo.veilarb.dokument.client.impl

import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.junit.WireMockRule
import no.nav.common.auth.context.AuthContextHolder
import no.nav.common.rest.client.RestClient
import no.nav.common.utils.fn.UnsafeSupplier
import no.nav.fo.veilarb.dokument.client.api.BrevClient
import no.nav.fo.veilarb.dokument.domain.Adresse
import no.nav.fo.veilarb.dokument.domain.MalType
import no.nav.fo.veilarb.dokument.domain.Målform
import no.nav.fo.veilarb.dokument.utils.TestUtils
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
                dato = "20. januar 2020",
                malform = Målform.NB,
                begrunnelse = listOf("Avsnitt 1", "Avsnitt 2"),
                kilder = listOf("Kilde 1", "Kilde 2"),
                mottaker = BrevClient.Mottaker(
                        navn = "Mottaker Navn",
                        Adresse(adresselinje1 = "Mottaker adresselinje 1",
                                adresselinje2 = "Mottaker adresselinje 2",
                                adresselinje3 = "Mottaker adresselinje 3",
                                postnummer = "1234",
                                poststed = "Mottaker Poststed")
                ),
                returadresse = BrevClient.Returadresse(adresselinje = "Retur adresselinje",
                        postnummer = "4321",
                        poststed = "Retur poststed"))


        val documentResponse = "document"

        val forventetInnhold =
                """
                    {
                      "malType": "SITUASJONSBESTEMT_INNSATS_SKAFFE_ARBEID",
                      "veilederNavn": "Veileder Navn",
                      "navKontor": "Nav kontor",
                      "kontaktEnhetNavn": "Nav kontor kontakt",
                      "dato": "20. januar 2020",
                      "malform": "NB",
                      "begrunnelse": ["Avsnitt 1", "Avsnitt 2"],
                      "kilder": ["Kilde 1", "Kilde 2"],
                      "mottaker": {
                        "navn": "Mottaker Navn",
                        "adresse": {
                          "adresselinje1": "Mottaker adresselinje 1",
                          "adresselinje2": "Mottaker adresselinje 2",
                          "adresselinje3": "Mottaker adresselinje 3",
                          "postnummer": "1234",
                          "poststed": "Mottaker Poststed"
                        }
                      },
                      "returadresse": {
                        "adresselinje": "Retur adresselinje",
                        "postnummer": "4321",
                        "poststed": "Retur poststed"
                      }
                    }
                """


        WireMock.givenThat(
                WireMock.post(WireMock.urlEqualTo("/api/v1/genpdf/vedtak14a/vedtak14a"))
                        .withRequestBody(WireMock.equalToJson(forventetInnhold))
                        .willReturn(WireMock.aResponse()
                                .withStatus(200)
                                .withBody(documentResponse.toByteArray())
                                .withHeader("Content-Type", "application/pdf")
                        ))


        val response = AuthContextHolder.withContext(TestUtils.authContext("test"), UnsafeSupplier {
            brevClient.genererBrev(brevdata)
        })

        assertEquals(documentResponse, response.decodeToString())
    }
}

