package no.nav.fo.veilarb.dokument.service;

import no.nav.fo.veilarb.dokument.domain.Sak;
import no.nav.sbl.dialogarena.test.junit.SystemPropertiesRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;

import static no.nav.fo.veilarb.dokument.ApplicationConfig.SAK_API_URL;
import static no.nav.fo.veilarb.dokument.service.SakService.ARENA_KODE;
import static no.nav.fo.veilarb.dokument.service.SakService.OPPFOLGING_KODE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SakServiceTest {

    private Client restClient = mock(Client.class);
    private SakService sakService;

    @Rule
    public SystemPropertiesRule systemPropertiesRule = new SystemPropertiesRule();

    @Before
    public void setup() {
        systemPropertiesRule.setProperty(SAK_API_URL, "test");
        sakService = new SakService(restClient);
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
                sakService.finnOppfolgingssaker("aktorId").stream().map(Sak::id)
        ).containsExactly(2, 9);
    }

    private void mockRestClient(List<Sak> saker) {
        WebTarget target = mock(WebTarget.class);
        Invocation.Builder builder = mock(Invocation.Builder.class);
        Response response = mock(Response.class);
        when(target.queryParam(any(), any())).thenReturn(target);
        when(target.request()).thenReturn(builder);
        when(builder.get()).thenReturn(response);
        when(response.readEntity(any(GenericType.class))).thenReturn(saker);
        when(restClient.target(any(String.class))).thenReturn(target);

    }
}