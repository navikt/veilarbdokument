package no.nav.fo.veilarb.dokument.utils;

import no.nav.apiapp.feil.Feil;
import no.nav.common.auth.SubjectHandler;
import no.nav.sbl.rest.RestUtils;
import no.nav.sbl.util.EnvironmentUtils;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

import static no.nav.apiapp.feil.FeilType.INGEN_TILGANG;
import static no.nav.apiapp.util.UrlUtils.clusterUrlForApplication;
import static no.nav.common.auth.SsoToken.Type.OIDC;
import static org.apache.cxf.helpers.HttpHeaderHelper.AUTHORIZATION;

@Component
public class VeilarbAbacServiceClient {

    @Inject
    public VeilarbAbacServiceClient() {
    }

    private final String abacTargetUrl = EnvironmentUtils.getOptionalProperty("VEILARBABAC")
            .orElseGet(() -> clusterUrlForApplication("veilarbabac"));

    public boolean veilederHarLesetilgangTilPerson(String aktorId) {
        String veilederToken = SubjectHandler.getSsoToken(OIDC).orElseThrow(() -> new Feil(INGEN_TILGANG));


        String response = RestUtils.withClient(client ->
                client.target(abacTargetUrl)
                        .path("self/person")
                        .queryParam("aktorId", aktorId)
                        .queryParam("action", "read")
                        .request()
                        .header(AUTHORIZATION, "Bearer " + veilederToken)
                        .header("subject", veilederToken)
                        .get(String.class)
        );

        return "permit".equals(response);

    }
}
