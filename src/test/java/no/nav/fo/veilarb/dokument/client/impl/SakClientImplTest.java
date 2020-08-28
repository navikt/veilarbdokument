package no.nav.fo.veilarb.dokument.client.impl;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import lombok.SneakyThrows;
import no.nav.common.auth.subject.IdentType;
import no.nav.common.auth.subject.SsoToken;
import no.nav.common.auth.subject.Subject;
import no.nav.common.auth.subject.SubjectHandler;
import no.nav.common.json.JsonUtils;
import no.nav.common.rest.client.RestClient;
import no.nav.fo.veilarb.dokument.client.api.SakClient;
import no.nav.fo.veilarb.dokument.domain.Sak;
import okhttp3.OkHttpClient;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static no.nav.fo.veilarb.dokument.client.impl.SakClientImpl.ARENA_KODE;
import static no.nav.fo.veilarb.dokument.client.impl.SakClientImpl.OPPFOLGING_KODE;
import static org.assertj.core.api.Assertions.assertThat;

public class SakClientImplTest {

    private SakClient sakClient;
    private Subject subject = new Subject("test", IdentType.InternBruker, SsoToken.oidcToken("token", new HashMap<>()));

    @Rule
    public WireMockRule wireMockRule = new WireMockRule();

    @Before
    public void setup() {
        OkHttpClient client = RestClient.baseClient();
        sakClient = new SakClientImpl(client, "http://localhost:" + wireMockRule.port());
    }

    @Test
    public void finnOppfolgingssaker__filtrerer_oppfolgingssaker() {


        List<Sak> saker = Arrays.asList(
                new Sak(7, OPPFOLGING_KODE, ARENA_KODE, null, null),
                new Sak(2, OPPFOLGING_KODE, ARENA_KODE, "2", null),
                new Sak(4, OPPFOLGING_KODE, "FS22", "4", null),
                new Sak(8, OPPFOLGING_KODE, ARENA_KODE, "", null),
                new Sak(9, OPPFOLGING_KODE, ARENA_KODE, "6", null),
                new Sak(3, OPPFOLGING_KODE, null, null, null),
                new Sak(6, null, null, null, null)
        );

        mockSakerResponse(saker);

        Stream<Integer> oppfolgingssaker = SubjectHandler.withSubject(subject, () ->
                sakClient.hentOppfolgingssaker("aktorId").stream().map(Sak::id));

        assertThat(oppfolgingssaker).containsExactly(2, 9);
    }

    @SneakyThrows
    private void mockSakerResponse(List<Sak> saker) {
        String json = JsonUtils.toJson(saker);
        givenThat(
                get(urlPathEqualTo("/api/v1/saker"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader("Content-Type", "applicaition/json")
                                .withBody(json)
                        ));
    }
}
