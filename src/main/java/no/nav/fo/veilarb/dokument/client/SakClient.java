package no.nav.fo.veilarb.dokument.client;

import no.nav.common.health.HealthCheck;
import no.nav.common.health.HealthCheckResult;
import no.nav.common.health.HealthCheckUtils;
import no.nav.common.rest.client.RestClient;
import no.nav.common.rest.client.RestUtils;
import no.nav.common.utils.StringUtils;
import no.nav.fo.veilarb.dokument.domain.OpprettSakDto;
import no.nav.fo.veilarb.dokument.domain.Sak;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static no.nav.common.utils.UrlUtils.joinPaths;
import static no.nav.fo.veilarb.dokument.util.AuthUtils.createBearerToken;

public class SakClient implements HealthCheck {

    private final OkHttpClient client;
    private final String host;

    public static String ARENA_KODE = "AO01";
    public static String OPPFOLGING_KODE = "OPP";

    public SakClient(OkHttpClient client, String sakUrl) {
        this.client = client;
        this.host = sakUrl;
    }

    public List<Sak> hentOppfolgingssaker(String aktorId) {
        HttpUrl url = HttpUrl.parse(host).newBuilder()
                .addPathSegments("api/v1/saker")
                .addQueryParameter("aktoerId", aktorId)
                .addQueryParameter("tema", OPPFOLGING_KODE)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .header(HttpHeaders.AUTHORIZATION, createBearerToken())
                .build();

        try (Response response = client.newCall(request).execute()) {
            RestUtils.throwIfNotSuccessful(response);
            List<Sak> saker = RestUtils.parseJsonResponseArrayOrThrow(response, Sak.class);
            return filtrerOppfolgingssaker(saker);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Feil ved kall mot GET " + request.url().toString(), e);
        }
    }

    public Sak opprettOppfolgingssak(String aktorId, String fagsakNr) {
        OpprettSakDto entity = new OpprettSakDto(OPPFOLGING_KODE, ARENA_KODE, aktorId, fagsakNr);

        Request request = new Request.Builder()
                .url(joinPaths(host, "/api/v1/saker"))
                .header(HttpHeaders.AUTHORIZATION, createBearerToken())
                .post(RestUtils.toJsonRequestBody(entity))
                .build();

        try (Response response = RestClient.baseClient().newCall(request).execute()) {
            RestUtils.throwIfNotSuccessful(response);
            return RestUtils.parseJsonResponseOrThrow(response, Sak.class);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Feil ved kall mot POST " + request.url().toString(), e);
        }
    }

    private static List<Sak> filtrerOppfolgingssaker(List<Sak> saker) {

        return saker.stream().filter(sak ->
                Objects.equals(sak.tema(), OPPFOLGING_KODE) &&
                        StringUtils.notNullOrEmpty(sak.fagsakNr()) &&
                        Objects.equals(sak.applikasjon(), ARENA_KODE))
                .collect(Collectors.toList());

    }

    @Override
    public HealthCheckResult checkHealth() {
        return HealthCheckUtils.pingUrl(joinPaths(host, "/internal/alive"), client);
    }
}
