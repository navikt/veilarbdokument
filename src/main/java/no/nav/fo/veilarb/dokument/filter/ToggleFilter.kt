package no.nav.fo.veilarb.dokument.filter

import no.nav.common.featuretoggle.UnleashClient
import no.nav.fo.veilarb.dokument.util.Toggles.VEILARBDOKUMENT_ENABLED_TOGGLE
import javax.servlet.*
import javax.servlet.http.HttpServletRequest

class ToggleFilter(private val unleashClient: UnleashClient) : Filter {

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val httpServletRequest = request as HttpServletRequest

        if (
            httpServletRequest.servletPath.startsWith("/internal") ||
            unleashClient.isEnabled(VEILARBDOKUMENT_ENABLED_TOGGLE)
        ) {
            chain.doFilter(request, response)
        } else {
            throw IllegalStateException("ikke tilgjengelig")
        }
    }

    override fun init(filterConfig: FilterConfig) {}
    override fun destroy() {}

}
