package no.nav.fo.veilarb.dokument.client.impl;

import no.nav.common.rest.client.RestUtils;
import no.nav.fo.veilarb.dokument.client.api.EnhetClient;
import no.nav.fo.veilarb.dokument.domain.EnhetKontaktinformasjon;
import no.nav.fo.veilarb.dokument.domain.EnhetOrganisering;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static no.nav.common.utils.UrlUtils.joinPaths;
import static no.nav.fo.veilarb.dokument.config.CacheConfig.NORG2_ENHET_KONTAKTINFO_CACHE_NAME;
import static no.nav.fo.veilarb.dokument.config.CacheConfig.NORG2_ENHET_ORGANISERING_CACHE_NAME;

public class EnhetClientImpl implements EnhetClient {

    private final OkHttpClient client;
    private final String host;

    public EnhetClientImpl(OkHttpClient client, String norgUrl) {
        this.client = client;
        host = norgUrl;
    }

    @Cacheable(NORG2_ENHET_KONTAKTINFO_CACHE_NAME)
    public EnhetKontaktinformasjon hentKontaktinfo(String enhetId) {
        Request request = new Request.Builder()
                .url(joinPaths(host, String.format("v1/enhet/%s/kontaktinformasjon", enhetId)))
                .build();

        try (Response response = client.newCall(request).execute()) {
            RestUtils.throwIfNotSuccessful(response);
            return RestUtils.parseJsonResponseOrThrow(response, EnhetKontaktinformasjon.class);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Feil ved kall mot " + request.url().toString(), e);
        }
    }

    @Cacheable(NORG2_ENHET_ORGANISERING_CACHE_NAME)
    public List<EnhetOrganisering> hentEnhetOrganisering(String enhetId) {

        Request request = new Request.Builder()
                .url(joinPaths(host, String.format("v1/enhet/%s/organisering", enhetId)))
                .build();

        try (Response response = client.newCall(request).execute()) {
            RestUtils.throwIfNotSuccessful(response);
            return RestUtils.parseJsonResponseArrayOrThrow(response, EnhetOrganisering.class);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Feil ved kall mot " + request.url().toString(), e);
        }
    }
}
