package no.nav.fo.veilarb.dokument.client.impl

import com.github.tomakehurst.wiremock.junit.WireMockRule
import no.nav.common.auth.context.AuthContextHolder
import no.nav.common.rest.client.RestClient
import no.nav.common.types.identer.Fnr
import no.nav.common.utils.fn.UnsafeSupplier
import no.nav.fo.veilarb.dokument.client.api.PersonClient
import no.nav.fo.veilarb.dokument.domain.Målform
import no.nav.fo.veilarb.dokument.util.TestUtils
import no.nav.fo.veilarb.dokument.util.TestUtils.givenWiremockOkJsonResponse
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PersonClientImplTest {

    lateinit var personClient: PersonClient

    private val wireMockRule = WireMockRule()

    @Rule
    fun getWireMockRule() = wireMockRule

    @Before
    fun setup() {
        val client = RestClient.baseClient()
        personClient = PersonClientImpl(client, "http://localhost:" + getWireMockRule().port())
    }

    @Test
    fun mapper_respons_riktig() {
        val fnr = Fnr("123")
        val jsonResponse =
                """{
                        "malform": "NN"
                   }"""

        givenWiremockOkJsonResponse("/api/v2/person/malform?fnr=$fnr", jsonResponse)

        val respons = AuthContextHolder.withContext(TestUtils.authContext("test"), UnsafeSupplier {
            personClient.hentMålform(fnr)
        })

        assertEquals(Målform.NN, respons)
    }
}
