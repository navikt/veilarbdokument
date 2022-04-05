package no.nav.fo.veilarb.dokument.client.impl;

import no.nav.common.auth.context.AuthContextHolder;
import no.nav.common.health.HealthCheckResult;
import no.nav.common.health.HealthCheckUtils;
import no.nav.common.rest.client.RestUtils;
import no.nav.common.types.identer.EnhetId;
import no.nav.common.types.identer.Fnr;
import no.nav.fo.veilarb.dokument.client.api.ArenaClient;
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

public class ArenaClientImpl implements ArenaClient {
    private final OkHttpClient client;
    private final String veilarbarenaUrl;
    private final AuthContextHolder authContextHolder;

    public ArenaClientImpl(OkHttpClient client, String veilarbarenaUrl, AuthContextHolder authContextHolder) {
        this.client = client;
        this.veilarbarenaUrl = veilarbarenaUrl;
        this.authContextHolder = authContextHolder;
    }

    public EnhetId oppfolgingsenhet(Fnr fnr) {
        Request request = new Request.Builder()
                .url(joinPaths(veilarbarenaUrl, "api", "oppfolgingsbruker", fnr.get()))
                .header(HttpHeaders.AUTHORIZATION, createBearerToken(authContextHolder))
                .build();

        try (Response response = client.newCall(request).execute()) {
            RestUtils.throwIfNotSuccessful(response);
            return RestUtils.parseJsonResponseOrThrow(response, OppfolgingsenhetDto.class).getNavKontor();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Feil ved kall mot" + request.url(), e);
        }
    }

    public Optional<ArenaOppfolgingssak> oppfolgingssak(Fnr fnr) {
        Request request = new Request.Builder()
                .url(joinPaths(veilarbarenaUrl, "api", "oppfolgingssak", fnr.get()))
                .header(HttpHeaders.AUTHORIZATION, createBearerToken(authContextHolder))
                .build();

        try (Response response = client.newCall(request).execute()) {
            RestUtils.throwIfNotSuccessful(response);
            return RestUtils.parseJsonResponse(response, ArenaOppfolgingssak.class);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Feil ved kall mot" + request.url(), e);
        }
    }

    @Override
    public HealthCheckResult checkHealth() {
        return HealthCheckUtils.pingUrl(joinPaths(veilarbarenaUrl, "internal", "isAlive"), client);
    }
}
