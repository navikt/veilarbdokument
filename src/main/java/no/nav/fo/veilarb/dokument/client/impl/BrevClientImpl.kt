package no.nav.fo.veilarb.dokument.client.impl

import no.nav.common.rest.client.RestUtils
import no.nav.common.utils.UrlUtils.joinPaths
import no.nav.fo.veilarb.dokument.client.api.BrevClient
import no.nav.fo.veilarb.dokument.util.AuthUtils.createBearerToken
import okhttp3.OkHttpClient
import okhttp3.Request
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class BrevClientImpl(val client: OkHttpClient, val pdfGenUrl: String) : BrevClient {

    override fun genererBrev(brevdataPdf: BrevClient.Brevdata): ByteArray {

        val request = Request.Builder()
                .url(joinPaths(pdfGenUrl, "api/v1/genpdf/vedtak14a/vedtak14a"))
                .header(HttpHeaders.AUTHORIZATION, createBearerToken())
                .build()

        try {
            client.newCall(request).execute().use { response ->
                RestUtils.throwIfNotSuccessful(response)
                return response.body()!!.bytes()
            }
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Feil ved kall mot" + request.url().toString(), e)
        }
    }
}
