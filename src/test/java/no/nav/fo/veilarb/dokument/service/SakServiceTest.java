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
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static no.nav.fo.veilarb.dokument.ApplicationConfig.SAK_API_URL;
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

    private static ZonedDateTime NYESTE_DATO = ZonedDateTime.of(2018, 4, 12, 16, 32, 22, 234, ZoneId.systemDefault());
    private static ZonedDateTime ELDRE_DATO = ZonedDateTime.of(2018, 3, 28, 18, 42, 12, 456, ZoneId.systemDefault());
    private static ZonedDateTime ELDSTE_DATO = ZonedDateTime.of(2017, 9, 6, 10, 2, 57, 687, ZoneId.systemDefault());

    @Test
    public void finnGjeldendeOppfolgingssak__henter_siste_oppfolgingssak() {


        List<Sak> saker = Arrays.asList(
                new Sak(7, "OPP", "AO01", null, NYESTE_DATO),
                new Sak(5, "OPP", "AO01", "5", NYESTE_DATO),
                new Sak(1, "OPP", "AO01", "1", ELDSTE_DATO),
                new Sak(4, "OPP", "FS22", "4", NYESTE_DATO),
                new Sak(3, "OPP", null, null, NYESTE_DATO),
                new Sak(6, null, null, null, NYESTE_DATO),
                new Sak(2, "OPP", "AO01", "2", ELDRE_DATO)
        );

        mockRestClient(saker);

        assertThat(sakService.finnGjeldendeOppfolgingssak("aktorId").id()).isEqualTo(5);
    }

    @Test(expected = IllegalStateException.class)
    public void finnGjeldendeOppfolgingssak__feiler_ved_kall_med_saker_uten_oppfolgingssak() {
        List<Sak> saker = Collections.singletonList(
                new Sak(4, "OPP", "FS22", "4", NYESTE_DATO)
        );

        mockRestClient(saker);

        sakService.finnGjeldendeOppfolgingssak("aktorId").id();
    }

    @Test(expected = IllegalStateException.class)
    public void finnGjeldendeOppfolgingssak__feiler_ved_kall_uten_saker() {
        List<Sak> saker = Collections.emptyList();

        mockRestClient(saker);

        sakService.finnGjeldendeOppfolgingssak("aktorId").id();
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