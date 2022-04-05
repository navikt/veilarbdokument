package no.nav.fo.veilarb.dokument.client.impl

import com.github.tomakehurst.wiremock.junit.WireMockRule
import no.nav.common.auth.context.AuthContextHolder
import no.nav.common.auth.context.AuthContextHolderThreadLocal
import no.nav.common.rest.client.RestClient
import no.nav.common.types.identer.EnhetId
import no.nav.common.utils.fn.UnsafeSupplier
import no.nav.fo.veilarb.dokument.client.api.VeilederClient
import no.nav.fo.veilarb.dokument.util.TestUtils.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class VeilederClientImplTest {

    val enhetNavn = "Enhet Navn"
    val veilederNavn = "Veileder Navn"
    val enhetId = EnhetId("123")

    lateinit var authContextHolder: AuthContextHolder
    lateinit var veilederClient: VeilederClient

    private val wireMockRule = WireMockRule()

    @Rule
    fun getWireMockRule() = wireMockRule

    @Before
    fun setup() {
        authContextHolder = AuthContextHolderThreadLocal.instance()
        veilederClient = VeilederClientImpl(
            RestClient.baseClient(), "http://localhost:" + getWireMockRule().port(),authContextHolder
        )
    }

    @Test
    fun henter_navn_pa_veileder() {
        val json = readTestResourceFile("veilarbveileder/veileder_response.json")
                .replace("VEILEDER_NAVN", veilederNavn)

        givenWiremockOkJsonResponse("/api/veileder/me", json)

        val veilederNavnResponse = authContextHolder.withContext(authContext("test"), UnsafeSupplier {
            veilederClient.hentVeiledernavn()
        })

        assertEquals(veilederNavn, veilederNavnResponse)
    }
}
