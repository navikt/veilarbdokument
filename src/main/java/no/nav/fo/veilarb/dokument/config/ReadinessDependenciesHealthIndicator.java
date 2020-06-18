package no.nav.fo.veilarb.dokument.config;

import no.nav.common.health.HealthCheckResult;
import no.nav.fo.veilarb.dokument.helsesjekk.DokumentproduksjonV3Helsesjekk;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component("readinessDependencies")
public class ReadinessDependenciesHealthIndicator implements HealthIndicator {

    private final DokumentproduksjonV3Helsesjekk dokumentproduksjonV3Helsesjekk;

    public ReadinessDependenciesHealthIndicator(DokumentproduksjonV3Helsesjekk dokumentproduksjonV3Helsesjekk) {
        this.dokumentproduksjonV3Helsesjekk = dokumentproduksjonV3Helsesjekk;
    }

    @Override
    public Health health() {
        HealthCheckResult healthCheckResult = dokumentproduksjonV3Helsesjekk.checkHealth();
        if (healthCheckResult.isUnhealthy()) {
            return Health.down().withDetail("Error message", healthCheckResult.getErrorMessage().orElse("")).build();
        }
        return Health.up().build();
    }

}
