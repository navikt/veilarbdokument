package no.nav.fo.veilarb.dokument.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import no.nav.common.json.JsonMapper
import no.nav.common.rest.client.RestUtils
import okhttp3.Response

object JsonUtils {
    val objectMapper: ObjectMapper = JsonMapper.defaultObjectMapper().registerModule(KotlinModule())
}

inline fun <reified T> Response.deserializeJson(): T? {
    return RestUtils.getBodyStr(this)
            .map { JsonUtils.objectMapper.readValue(it, T::class.java) }
            .orElse(null)
}

inline fun <reified T> Response.deserializeJsonOrThrow(): T {
    return this.deserializeJson() ?: throw IllegalStateException("Unable to parse JSON object from response body")
}
