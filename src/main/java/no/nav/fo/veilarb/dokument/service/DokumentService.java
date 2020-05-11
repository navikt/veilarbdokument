package no.nav.fo.veilarb.dokument.service;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import no.nav.brukerdialog.security.domain.IdentType;
import no.nav.common.auth.Subject;
import no.nav.common.auth.SubjectHandler;
import no.nav.fo.veilarb.dokument.client.VeilederClient;
import no.nav.fo.veilarb.dokument.domain.*;
import no.nav.fo.veilarb.dokument.mappers.DokumentutkastMapper;
import no.nav.fo.veilarb.dokument.mappers.IkkeredigerbartDokumentMapper;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v3.DokumentproduksjonV3;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v3.meldinger.WSProduserDokumentutkastRequest;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v3.meldinger.WSProduserIkkeredigerbartDokumentRequest;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v3.meldinger.WSProduserIkkeredigerbartDokumentResponse;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Slf4j
@Component
public class DokumentService {

    private final DokumentproduksjonV3 dokumentproduksjon;
    private final AuthService authService;
    private final VeilederClient veilederClient;
    private final OppfolgingssakService oppfolgingssakService;
    private final KontaktEnhetService kontaktEnhetService;

    @Inject
    public DokumentService(DokumentproduksjonV3 dokumentproduksjon,
                           AuthService authService,
                           VeilederClient veilederClient,
                           OppfolgingssakService oppfolgingssakService,
                           KontaktEnhetService kontaktEnhetService) {
        this.dokumentproduksjon = dokumentproduksjon;
        this.authService = authService;
        this.veilederClient = veilederClient;
        this.oppfolgingssakService = oppfolgingssakService;
        this.kontaktEnhetService = kontaktEnhetService;
    }

    public DokumentbestillingResponsDto bestillDokument(DokumentbestillingDto dto) {
        Bruker bruker = authService.sjekkTilgang(dto.brukerFnr(), dto.enhetId());

        Dokumentbestilling dokumentbestilling = lagDokumentbestilling(dto, bruker);
        return produserIkkeredigerbartDokument(dokumentbestilling, bruker);
    }

    private Dokumentbestilling lagDokumentbestilling(DokumentbestillingDto dto, Bruker bruker) {
        Brevdata brevdata = lagBrevdata(dto);
        Sak sak = oppfolgingssakService.hentOppfolgingssak(bruker);

        return Dokumentbestilling.builder()
                .brevdata(brevdata)
                .sak(sak)
                .veilederNavn(brevdata.veilederNavn())
                .build();
    }

    private Brevdata lagBrevdata(DokumentbestillingDto dokumentbestilling) {
        String veilederNavn = veilederClient.hentVeiledernavn();

        String enhetIdKontakt = kontaktEnhetService.utledKontaktEnhetId(dokumentbestilling.enhetId());

        return Brevdata.builder()
                .brukerFnr(dokumentbestilling.brukerFnr())
                .malType(dokumentbestilling.malType())
                .enhetId(dokumentbestilling.enhetId())
                .enhetIdKontakt(enhetIdKontakt)
                .veilederId(getVeilederId())
                .veilederNavn(veilederNavn)
                .begrunnelse(dokumentbestilling.begrunnelse())
                .kilder(dokumentbestilling.opplysninger())
                .build();
    }

    @SneakyThrows
    private DokumentbestillingResponsDto produserIkkeredigerbartDokument(Dokumentbestilling dokumentbestilling, Bruker bruker) {
        WSProduserIkkeredigerbartDokumentRequest request =
                IkkeredigerbartDokumentMapper.mapRequest(dokumentbestilling);

        try {
            WSProduserIkkeredigerbartDokumentResponse response = dokumentproduksjon.produserIkkeredigerbartDokument(request);
            MetrikkService.rapporterDokumentbestilling(dokumentbestilling.brevdata().malType());
            return IkkeredigerbartDokumentMapper.mapRespons(response);
        } catch (Exception e) {
            log.error(String.format("Kunne ikke produsere dokument for aktorId %s", bruker.getAktorId()), e);
            MetrikkService.rapporterFeilendeDokumentbestilling(dokumentbestilling.brevdata().malType());
            throw e;
        }

    }

    private String getVeilederId() {
        return SubjectHandler.getSubject()
                .filter(subject -> IdentType.InternBruker.equals(subject.getIdentType()))
                .map(Subject::getUid)
                .orElseThrow(() -> new IllegalStateException("Mangler veileder token"));
    }

    @SneakyThrows
    public byte[] produserDokumentutkast(DokumentbestillingDto dto) {
        Bruker bruker = authService.sjekkTilgang(dto.brukerFnr(), dto.enhetId());

        Brevdata brevdata = lagBrevdata(dto);
        WSProduserDokumentutkastRequest dokumentutkastRequest =
                DokumentutkastMapper.produserDokumentutkastRequest(brevdata);

        try {
            byte[] dokumentutkast = dokumentproduksjon.produserDokumentutkast(dokumentutkastRequest).getDokumentutkast();
            MetrikkService.rapporterDokumentutkast(dto.malType());
            return dokumentutkast;
        } catch (Exception e) {
            log.error(String.format("Kunne ikke produsere dokumentutkast for aktorId %s", bruker.getAktorId()), e);
            MetrikkService.rapporterFeilendeDokumentutkast(dto.malType());
            throw e;
        }
    }

}
