package no.nav.fo.veilarb.dokument.client.impl;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import no.nav.common.health.HealthCheckResult;
import no.nav.common.health.HealthCheckUtils;
import no.nav.common.rest.client.RestUtils;
import no.nav.common.types.identer.EnhetId;
import no.nav.fo.veilarb.dokument.client.api.VeilederClient;
import no.nav.fo.veilarb.dokument.domain.VeilederDto;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static no.nav.common.utils.UrlUtils.joinPaths;
import static no.nav.fo.veilarb.dokument.util.AuthUtils.createBearerToken;

@Slf4j
public class VeilederClientImpl implements VeilederClient {

    private final OkHttpClient client;
    private final String veilarbveilederUrl;

    public VeilederClientImpl(OkHttpClient client, String veilarbveilederUrl) {
        this.client = client;
        this.veilarbveilederUrl = veilarbveilederUrl;
    }

    public String hentVeiledernavn() {

        Request request = new Request.Builder()
                .url(joinPaths(veilarbveilederUrl, "api", "veileder", "me"))
                .header(HttpHeaders.AUTHORIZATION, createBearerToken())
                .build();

        try (Response response = client.newCall(request).execute()) {
            RestUtils.throwIfNotSuccessful(response);
            return RestUtils.parseJsonResponseOrThrow(response, VeilederDto.class).navn();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Feil ved kall mot" + request.url().toString(), e);
        }
    }

    @Override
    public HealthCheckResult checkHealth() {
        return HealthCheckUtils.pingUrl(joinPaths(veilarbveilederUrl, "internal", "isAlive"), client);
    }
}

@Value
class VeilarbveilederEnhetDto {
    EnhetId enhetId;
    String navn;
}
