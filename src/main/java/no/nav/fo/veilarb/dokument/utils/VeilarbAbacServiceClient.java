package no.nav.fo.veilarb.dokument.utils;

import no.nav.sbl.rest.RestUtils;
import no.nav.sbl.util.EnvironmentUtils;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

import static no.nav.apiapp.util.UrlUtils.clusterUrlForApplication;
import static org.apache.cxf.helpers.HttpHeaderHelper.AUTHORIZATION;

@Component
public class VeilarbAbacServiceClient {

    @Inject
    public VeilarbAbacServiceClient() {
    }

    private final String abacTargetUrl = EnvironmentUtils.getOptionalProperty("VEILARBABAC")
            .orElseGet(() -> clusterUrlForApplication("veilarbabac"));

    public boolean harLesetilgangTilPerson(String veilederOidcToken, String aktorId) {

        String response = RestUtils.withClient(client ->
                client.target(abacTargetUrl)
                        .path("self/person")
                        .queryParam("aktorId", aktorId)
                        .queryParam("action", "read")
                        .request()
                        .header(AUTHORIZATION, "Bearer " + veilederOidcToken)
                        .get(String.class)
        );

        return "permit".equals(response);

    }
}
