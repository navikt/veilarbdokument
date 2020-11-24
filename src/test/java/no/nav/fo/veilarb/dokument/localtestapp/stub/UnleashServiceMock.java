package no.nav.fo.veilarb.dokument.localtestapp.stub;

import no.nav.common.featuretoggle.UnleashService;
import no.nav.common.health.HealthCheckResult;

import static no.nav.fo.veilarb.dokument.localtestapp.stub.Values.*;
import static no.nav.fo.veilarb.dokument.util.Toggles.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UnleashServiceMock {

    public static UnleashService getMock() {
        UnleashService unleashService = mock(UnleashService.class);
        when(unleashService.isEnabled(PTO_VEDTAKSSTOTTE_PILOT_TOGGLE)).thenReturn(TEST_PTO_VEDTAKSSTOTTE_PILOT_TOGGLE);
        when(unleashService.isEnabled(VEILARBDOKUMENT_ENABLED_TOGGLE)).thenReturn(TEST_VEILARBDOKUMENT_ENABLED_TOGGLE);
        when(unleashService.isEnabled(VEILARBDOKUMENT_V2_API_ENABLED_TOGGLE)).thenReturn(TEST_VEILARBDOKUMENT_V2_API_ENABLED_TOGGLE);
        when(unleashService.checkHealth()).thenReturn(HealthCheckResult.healthy());
        return unleashService;
    }
}
