package no.nav.fo.veilarb.dokument.utils;

import no.nav.brukerdialog.security.oidc.SystemUserTokenProvider;
import no.nav.sbl.rest.RestUtils;
import no.nav.sbl.util.EnvironmentUtils;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

import static no.nav.apiapp.util.UrlUtils.clusterUrlForApplication;
import static org.apache.cxf.helpers.HttpHeaderHelper.AUTHORIZATION;

@Component
public class VeilarbAbacServiceClient {

    private final SystemUserTokenProvider systemUserTokenProvider;

    @Inject
    public VeilarbAbacServiceClient(SystemUserTokenProvider systemUserTokenProvider) {
        this.systemUserTokenProvider = systemUserTokenProvider;
    }

    private final String abacTargetUrl = EnvironmentUtils.getOptionalProperty("VEILARBABAC")
            .orElseGet(() -> clusterUrlForApplication("veilarbabac"));

    public boolean harLesetilgangTilPerson(String veilederOidcToken, String aktorId) {

        String response = RestUtils.withClient(client ->
                client.target(abacTargetUrl)
                        .path("person")
                        .queryParam("aktorId", aktorId)
                        .queryParam("action", "read")
                        .request()
                        .header(AUTHORIZATION, "Bearer " + systemUserTokenProvider.getToken())
                        .header("subject", veilederOidcToken)
                        .header("subjectType", "InternBruker")
                        .get(String.class)
        );

        return "permit".equals(response);

    }
}
