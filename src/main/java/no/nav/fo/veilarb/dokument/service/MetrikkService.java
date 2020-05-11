package no.nav.fo.veilarb.dokument.service;

import io.micrometer.core.instrument.MeterRegistry;
import no.nav.fo.veilarb.dokument.domain.MalType;
import no.nav.metrics.MetricsFactory;

public class MetrikkService {

    private static final MeterRegistry meterRegistry = MetricsFactory.getMeterRegistry();;

    public static void rapporterSak(String status) {
        meterRegistry.counter("sak", "status", status).increment();
    }

    public static void rapporterDokumentutkast(MalType mal) {
        rapporterDokumenthendelse("dokumentutkast", mal);
    }

    public static void rapporterFeilendeDokumentutkast(MalType mal) {
        rapporterDokumenthendelse("feilende_dokumentutkast", mal);
    }

    public static void rapporterDokumentbestilling(MalType mal) {
        rapporterDokumenthendelse("dokumentbestilling", mal);
    }

    public static void rapporterFeilendeDokumentbestilling(MalType mal) {
        rapporterDokumenthendelse("feilende_dokumentbestilling", mal);
    }

    private static void rapporterDokumenthendelse(String hendelse, MalType mal) {
        meterRegistry.counter(hendelse, "mal", mal.kode).increment();
    }
}
