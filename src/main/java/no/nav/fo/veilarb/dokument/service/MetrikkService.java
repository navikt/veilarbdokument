package no.nav.fo.veilarb.dokument.service;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import no.nav.fo.veilarb.dokument.domain.MalType;
import no.nav.metrics.MetricsFactory;

@Slf4j
public class MetrikkService {

    private static final MeterRegistry meterRegistry = MetricsFactory.getMeterRegistry();

    public static void rapporterSak(String status) {
        try {
            meterRegistry.counter("sak", "status", status).increment();
        } catch (Throwable t) {
            log.error("Feil ved rapportering av metrikk", t);
        }
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
        try {
            meterRegistry.counter(hendelse, "mal", mal.kode).increment();
        } catch (Throwable t) {
            log.error("Feil ved rapportering av metrikk", t);
        }
    }
}
