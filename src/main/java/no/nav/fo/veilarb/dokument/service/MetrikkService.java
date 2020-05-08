package no.nav.fo.veilarb.dokument.service;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;

public class MetrikkService {

    private static final MeterRegistry meterRegistry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);

    public static void rapporterSak(String status) {
        meterRegistry.counter("sak", "status", status);
    }

    public static void rapporterDokumentutkast(String mal) {
        rapporterDokumenthendelse("dokumentutkast", mal);
    }

    public static void rapporterFeilendeDokumentutkast(String mal) {
        rapporterDokumenthendelse("feilende_dokumentutkast", mal);
    }

    public static void rapporterDokumentbestilling(String mal) {
        rapporterDokumenthendelse("dokumentbestilling", mal);
    }

    public static void rapporterFeilendeDokumentbestilling(String mal) {
        rapporterDokumenthendelse("feilende_dokumentbestilling", mal);
    }

    private static void rapporterDokumenthendelse(String hendelse, String mal) {
        meterRegistry.counter(hendelse, "mal", mal).increment();
    }
}
