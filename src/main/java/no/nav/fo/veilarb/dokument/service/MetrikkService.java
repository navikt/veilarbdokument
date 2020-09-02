package no.nav.fo.veilarb.dokument.service;

import io.micrometer.core.instrument.MeterRegistry;
import no.nav.fo.veilarb.dokument.domain.MalType;
import org.springframework.stereotype.Service;

@Service
public class MetrikkService {

    MeterRegistry meterRegistry;

    public MetrikkService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    public void rapporterSak(String status) {
        meterRegistry.counter("sak", "status", status).increment();
    }

    public void rapporterFeilendeDokumentutkast(MalType mal) {
        rapporterDokumenthendelse("feilende_dokumentutkast", mal);
    }

    public void rapporterFeilendeDokumentbestilling(MalType mal) {
        rapporterDokumenthendelse("feilende_dokumentbestilling", mal);
    }

    private void rapporterDokumenthendelse(String hendelse, MalType mal) {
        meterRegistry.counter(hendelse, "mal", mal.kode).increment();
    }
}
