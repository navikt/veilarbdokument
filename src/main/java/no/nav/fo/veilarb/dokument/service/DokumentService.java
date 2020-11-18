package no.nav.fo.veilarb.dokument.service;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import no.nav.common.types.identer.EnhetId;
import no.nav.common.types.identer.NavIdent;
import no.nav.fo.veilarb.dokument.client.api.VeilederClient;
import no.nav.fo.veilarb.dokument.domain.*;
import no.nav.fo.veilarb.dokument.mappers.DokumentutkastMapper;
import no.nav.fo.veilarb.dokument.mappers.IkkeredigerbartDokumentMapper;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v3.DokumentproduksjonV3;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v3.meldinger.WSProduserDokumentutkastRequest;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v3.meldinger.WSProduserIkkeredigerbartDokumentRequest;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v3.meldinger.WSProduserIkkeredigerbartDokumentResponse;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DokumentService {

    private final DokumentproduksjonV3 dokumentproduksjon;
    private final AuthService authService;
    private final VeilederClient veilederClient;
    private final OppfolgingssakService oppfolgingssakService;
    private final KontaktEnhetService kontaktEnhetService;
    private final MetrikkService metrikkService;

    public DokumentService(DokumentproduksjonV3 dokumentproduksjon,
                           AuthService authService,
                           VeilederClient veilederClient,
                           OppfolgingssakService oppfolgingssakService,
                           KontaktEnhetService kontaktEnhetService,
                           MetrikkService metrikkService) {
        this.dokumentproduksjon = dokumentproduksjon;
        this.authService = authService;
        this.veilederClient = veilederClient;
        this.oppfolgingssakService = oppfolgingssakService;
        this.kontaktEnhetService = kontaktEnhetService;
        this.metrikkService = metrikkService;
    }

    public DokumentbestillingResponsDto bestillDokument(DokumentbestillingDto dto) {
        Bruker bruker = authService.sjekkTilgang(dto.getBrukerFnr(), dto.getEnhetId());

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
        NavIdent veilederIdent = authService.getInnloggetVeilederIdent();
        String veilederNavn = veilederClient.hentVeiledernavn();

        EnhetId enhetIdKontakt = kontaktEnhetService.utledEnhetKontaktinformasjon(dokumentbestilling.getEnhetId()).getEnhetNr();

        return Brevdata.builder()
                .brukerFnr(dokumentbestilling.getBrukerFnr())
                .malType(dokumentbestilling.getMalType())
                .enhetId(dokumentbestilling.getEnhetId())
                .enhetIdKontakt(enhetIdKontakt)
                .veilederId(veilederIdent)
                .veilederNavn(veilederNavn)
                .begrunnelse(dokumentbestilling.getBegrunnelse())
                .kilder(dokumentbestilling.getOpplysninger())
                .build();
    }

    @SneakyThrows
    private DokumentbestillingResponsDto produserIkkeredigerbartDokument(Dokumentbestilling dokumentbestilling, Bruker bruker) {
        WSProduserIkkeredigerbartDokumentRequest request =
                IkkeredigerbartDokumentMapper.mapRequest(dokumentbestilling);

        try {
            WSProduserIkkeredigerbartDokumentResponse response = dokumentproduksjon.produserIkkeredigerbartDokument(request);
            return IkkeredigerbartDokumentMapper.mapRespons(response);
        } catch (Exception e) {
            log.error(String.format("Kunne ikke produsere dokument for aktorId %s", bruker.getAktorId()), e);
            metrikkService.rapporterFeilendeDokumentbestilling(dokumentbestilling.brevdata().malType());
            throw e;
        }

    }

    @SneakyThrows
    public byte[] produserDokumentutkast(DokumentbestillingDto dto) {
        Bruker bruker = authService.sjekkTilgang(dto.getBrukerFnr(), dto.getEnhetId());

        Brevdata brevdata = lagBrevdata(dto);
        WSProduserDokumentutkastRequest dokumentutkastRequest =
                DokumentutkastMapper.produserDokumentutkastRequest(brevdata);

        try {
            return dokumentproduksjon.produserDokumentutkast(dokumentutkastRequest).getDokumentutkast();
        } catch (Exception e) {
            log.error(String.format("Kunne ikke produsere dokumentutkast for aktorId %s", bruker.getAktorId()), e);
            metrikkService.rapporterFeilendeDokumentutkast(dto.getMalType());
            throw e;
        }
    }

}
