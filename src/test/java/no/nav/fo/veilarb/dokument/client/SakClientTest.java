package no.nav.fo.veilarb.dokument.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import lombok.SneakyThrows;
import no.nav.fo.veilarb.dokument.domain.Sak;
import no.nav.json.JsonProvider;
import no.nav.sbl.dialogarena.test.junit.SystemPropertiesRule;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.client.Client;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static no.nav.fo.veilarb.dokument.config.ApplicationConfig.SAK_API_URL;
import static no.nav.fo.veilarb.dokument.client.SakClient.ARENA_KODE;
import static no.nav.fo.veilarb.dokument.client.SakClient.OPPFOLGING_KODE;
import static org.assertj.core.api.Assertions.assertThat;

public class SakClientTest {

    private Client restClient;
    private SakClient sakClient;

    @Rule
    public SystemPropertiesRule systemPropertiesRule = new SystemPropertiesRule();

    @Rule
    public WireMockRule wireMockRule = new WireMockRule();

    @Before
    public void setup() {
        systemPropertiesRule.setProperty(SAK_API_URL, "http://localhost:" + wireMockRule.port());
        restClient = new JerseyClientBuilder().build();
        sakClient = new SakClient(restClient);
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

        mockRestClient(saker);

        assertThat(
                sakClient.hentOppfolgingssaker("aktorId").stream().map(Sak::id)
        ).containsExactly(2, 9);
    }

    @SneakyThrows
    private void mockRestClient(List<Sak> saker) {
        ObjectMapper objectMapper = JsonProvider.createObjectMapper();

        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        objectMapper.writeValue(out, saker);
        final byte[] data = out.toByteArray();
        String response = new String(data);

        givenThat(
                get(urlPathEqualTo("/api/v1/saker"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader("Content-Type", "applicaition/json")
                                .withBody(response)
        ));
    }
}
