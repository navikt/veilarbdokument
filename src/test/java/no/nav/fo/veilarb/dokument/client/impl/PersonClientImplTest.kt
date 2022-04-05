package no.nav.fo.veilarb.dokument.client.impl

import com.github.tomakehurst.wiremock.junit.WireMockRule
import no.nav.common.auth.context.AuthContextHolder
import no.nav.common.auth.context.AuthContextHolderThreadLocal
import no.nav.common.rest.client.RestClient
import no.nav.common.types.identer.Fnr
import no.nav.common.utils.fn.UnsafeSupplier
import no.nav.fo.veilarb.dokument.client.api.PersonClient
import no.nav.fo.veilarb.dokument.domain.Målform
import no.nav.fo.veilarb.dokument.util.TestUtils
import no.nav.fo.veilarb.dokument.util.TestUtils.givenWiremockOkJsonResponse
import okhttp3.OkHttpClient
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PersonClientImplTest {

    lateinit var authContextHolder: AuthContextHolder
    lateinit var personClient: PersonClient

    private val wireMockRule = WireMockRule()

    @Rule
    fun getWireMockRule() = wireMockRule

    @Before
    fun setup() {
        authContextHolder = AuthContextHolderThreadLocal.instance()
        personClient = PersonClientImpl(
            RestClient.baseClient(), "http://localhost:" + getWireMockRule().port(), authContextHolder
        )
    }

    @Test
    fun mapper_respons_riktig() {
        val fnr = Fnr("123")
        val jsonResponse =
                """{
                        "malform": "NN"
                   }"""

        givenWiremockOkJsonResponse("/api/v2/person/malform?fnr=$fnr", jsonResponse)

        val respons = authContextHolder.withContext(TestUtils.authContext("test"), UnsafeSupplier {
            personClient.hentMålform(fnr)
        })

        assertEquals(Målform.NN, respons)
    }
}
