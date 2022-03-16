package no.nav.fo.veilarb.dokument.client.impl

import no.nav.common.health.HealthCheckResult
import no.nav.common.health.HealthCheckUtils
import no.nav.common.rest.client.RestUtils
import no.nav.common.types.identer.Fnr
import no.nav.common.utils.UrlUtils
import no.nav.fo.veilarb.dokument.client.api.PersonClient
import no.nav.fo.veilarb.dokument.domain.Målform
import no.nav.fo.veilarb.dokument.util.AuthUtils
import no.nav.fo.veilarb.dokument.util.deserializeJsonOrThrow
import okhttp3.OkHttpClient
import okhttp3.Request
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class PersonClientImpl(val client: OkHttpClient, val veilarbpersonUrl: String) : PersonClient {

    override fun hentMålform(fnr: Fnr): Målform {
        val request = Request.Builder()
                .url(UrlUtils.joinPaths(veilarbpersonUrl, "api/v2/person/malform?fnr=$fnr"))
                .header(HttpHeaders.AUTHORIZATION, AuthUtils.createBearerToken())
                .build()
        try {
            client.newCall(request).execute().use { response ->
                RestUtils.throwIfNotSuccessful(response)
                return response
                        .deserializeJsonOrThrow<MalformRespons>()
                        .tilMålform()
            }
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Feil ved kall mot" + request.url().toString(), e)
        }
    }

    override fun checkHealth(): HealthCheckResult {
        return HealthCheckUtils.pingUrl(UrlUtils.joinPaths(veilarbpersonUrl, "internal", "isAlive"), client)
    }

    data class MalformRespons(val malform: String?) {
        fun tilMålform(): Målform {
            return Målform.values().find { it.name == malform?.toUpperCase() } ?: Målform.NB
        }
    }
}
