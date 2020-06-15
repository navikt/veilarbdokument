package no.nav.fo.veilarb.dokument.helsesjekk;

import no.nav.common.health.HealthCheck;
import no.nav.common.health.HealthCheckResult;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v3.DokumentproduksjonV3;
import org.springframework.stereotype.Component;

@Component
public class DokumentproduksjonV3Helsesjekk implements HealthCheck {

    private final DokumentproduksjonV3 dokumentproduksjonV3;

    public DokumentproduksjonV3Helsesjekk(DokumentproduksjonV3 dokumentproduksjonV3) {
        this.dokumentproduksjonV3 = dokumentproduksjonV3;
    }

    @Override
    public HealthCheckResult checkHealth() {
        try {
            dokumentproduksjonV3.ping();
        } catch (Throwable t) {
            return HealthCheckResult.unhealthy(t);
        }
        return HealthCheckResult.healthy();
    }
}
