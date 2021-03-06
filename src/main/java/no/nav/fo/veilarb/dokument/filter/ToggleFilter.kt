package no.nav.fo.veilarb.dokument.filter

import no.nav.common.featuretoggle.UnleashService
import no.nav.common.utils.EnvironmentUtils
import no.nav.fo.veilarb.dokument.util.Toggles.VEILARBDOKUMENT_ENABLED_TOGGLE
import no.nav.fo.veilarb.dokument.util.Toggles.VEILARBDOKUMENT_V2_API_ENABLED_TOGGLE
import javax.servlet.*
import javax.servlet.http.HttpServletRequest

class ToggleFilter(private val unleashService: UnleashService) : Filter {

    private val isDevelopment = EnvironmentUtils.isDevelopment().orElse(false)

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val httpServletRequest = request as HttpServletRequest

        if (
            httpServletRequest.servletPath.startsWith("/internal") ||
            unleashService.isEnabled(VEILARBDOKUMENT_ENABLED_TOGGLE)
        ) {

            if (httpServletRequest.servletPath.startsWith("/api/v2") &&
                    !(isDevelopment && unleashService.isEnabled(VEILARBDOKUMENT_V2_API_ENABLED_TOGGLE))) {
                throw IllegalStateException("ikke tilgjengelig")
            }

            chain.doFilter(request, response)
        } else {
            throw IllegalStateException("ikke tilgjengelig")
        }
    }

    override fun init(filterConfig: FilterConfig) {}
    override fun destroy() {}

}
