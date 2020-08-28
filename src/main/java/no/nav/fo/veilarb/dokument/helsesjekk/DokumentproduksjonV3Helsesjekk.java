package no.nav.fo.veilarb.dokument.helsesjekk;

import lombok.extern.slf4j.Slf4j;
import no.nav.common.health.HealthCheck;
import no.nav.common.health.HealthCheckResult;
import no.nav.fo.veilarb.dokument.config.EnvironmentProperties;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v3.DokumentproduksjonV3;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DokumentproduksjonV3Helsesjekk implements HealthCheck {

    private final DokumentproduksjonV3 dokumentproduksjonV3;
    private final EnvironmentProperties properties;

    public DokumentproduksjonV3Helsesjekk(DokumentproduksjonV3 dokumentproduksjonV3, EnvironmentProperties properties) {
        this.dokumentproduksjonV3 = dokumentproduksjonV3;
        this.properties = properties;
    }

    @Override
    public HealthCheckResult checkHealth() {
        try {
            dokumentproduksjonV3.ping();
        } catch (Throwable t) {
            log.warn("Helsesjekk feilet mot " + properties.getDokumentproduksjonUrl(), t);
            return HealthCheckResult.unhealthy("Helsesjekk feilet mot " + properties.getDokumentproduksjonUrl(), t);
        }
        return HealthCheckResult.healthy();
    }
}
