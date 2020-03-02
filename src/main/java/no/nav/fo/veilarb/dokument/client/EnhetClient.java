package no.nav.fo.veilarb.dokument.client;

import no.nav.fo.veilarb.dokument.domain.EnhetKontaktinformasjon;
import no.nav.fo.veilarb.dokument.domain.EnhetOrganisering;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.GenericType;
import java.util.List;

import static no.nav.apiapp.util.UrlUtils.joinPaths;
import static no.nav.fo.veilarb.dokument.config.ApplicationConfig.NORG2_API_URL_PROPERTY;
import static no.nav.fo.veilarb.dokument.config.CacheConfig.NORG2_ENHET_KONTAKTINFO_CACHE_NAME;
import static no.nav.fo.veilarb.dokument.config.CacheConfig.NORG2_ENHET_ORGANISERING_CACHE_NAME;
import static no.nav.sbl.util.EnvironmentUtils.getRequiredProperty;

@Component
public class EnhetClient {

    private final Client restClient;
    private final String host;

    @Inject
    public EnhetClient(Client restClient) {
        this.restClient = restClient;
        host = getRequiredProperty(NORG2_API_URL_PROPERTY);
    }

    @Cacheable(NORG2_ENHET_KONTAKTINFO_CACHE_NAME)
    public EnhetKontaktinformasjon hentKontaktinfo(String enhetId) {
        return restClient
                .target(joinPaths(host, String.format("v1/enhet/%s/kontaktinformasjon", enhetId)))
                .request()
                .get(EnhetKontaktinformasjon.class);
    }

    @Cacheable(NORG2_ENHET_ORGANISERING_CACHE_NAME)
    public List<EnhetOrganisering> hentEnhetOrganisering(String enhetId) {
        return restClient
                .target(joinPaths(host, String.format("v1/enhet/%s/organisering", enhetId)))
                .request()
                .get()
                .readEntity(new GenericType<List<EnhetOrganisering>>() {
                });
    }
}
