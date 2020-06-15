package no.nav.fo.veilarb.dokument.client;

import lombok.extern.slf4j.Slf4j;
import no.nav.common.health.HealthCheck;
import no.nav.common.health.HealthCheckResult;
import no.nav.common.health.HealthCheckUtils;
import no.nav.common.rest.client.RestUtils;
import no.nav.fo.veilarb.dokument.domain.ArenaOppfolgingssak;
import no.nav.fo.veilarb.dokument.domain.OppfolgingsenhetDto;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static no.nav.common.utils.UrlUtils.joinPaths;
import static no.nav.fo.veilarb.dokument.util.AuthUtils.createBearerToken;

@Slf4j
public class ArenaClient implements HealthCheck {

    private final OkHttpClient client;
    private final String host;

    public ArenaClient(OkHttpClient client, String veilarbarenaUrl) {
        this.client = client;
        host = joinPaths(veilarbarenaUrl, "/veilarbarena/api");
    }

    public String oppfolgingsenhet(String fnr) {
        Request request = new Request.Builder()
                .url(joinPaths(host, "oppfolgingsbruker", fnr))
                .header(HttpHeaders.AUTHORIZATION, createBearerToken())
                .build();

        try (Response response = client.newCall(request).execute()) {
            RestUtils.throwIfNotSuccessful(response);
            return RestUtils.parseJsonResponseOrThrow(response, OppfolgingsenhetDto.class).getNavKontor();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Feil ved kall mot" + request.url().toString(), e);
        }
    }

    public Optional<ArenaOppfolgingssak> oppfolgingssak(String fnr) {
        Request request = new Request.Builder()
                .url(joinPaths(host, "oppfolgingssak", fnr))
                .header(HttpHeaders.AUTHORIZATION, createBearerToken())
                .build();

        try (Response response = client.newCall(request).execute()) {
            RestUtils.throwIfNotSuccessful(response);
            return RestUtils.parseJsonResponse(response, ArenaOppfolgingssak.class);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Feil ved kall mot" + request.url().toString(), e);
        }
    }

    @Override
    public HealthCheckResult checkHealth() {
        return HealthCheckUtils.pingUrl(joinPaths(host, "ping"), client);
    }
}
