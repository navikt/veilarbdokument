package no.nav.fo.veilarb.dokument.localtestapp.stub;

import no.nav.common.featuretoggle.UnleashService;

import static no.nav.fo.veilarb.dokument.localtestapp.stub.Values.TEST_UNLEASH_ENABLED;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UnleashServiceMock {

    public static UnleashService getMock() {
        UnleashService unleashService = mock(UnleashService.class);
        when(unleashService.isEnabled(any())).thenReturn(TEST_UNLEASH_ENABLED);
        return unleashService;
    }
}
