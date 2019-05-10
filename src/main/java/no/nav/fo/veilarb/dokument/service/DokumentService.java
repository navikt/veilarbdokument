package no.nav.fo.veilarb.dokument.service;

import lombok.SneakyThrows;
import no.nav.apiapp.security.veilarbabac.Bruker;
import no.nav.brukerdialog.security.domain.IdentType;
import no.nav.common.auth.Subject;
import no.nav.common.auth.SubjectHandler;
import no.nav.fo.veilarb.dokument.domain.*;
import no.nav.fo.veilarb.dokument.mappers.DokumentutkastMapper;
import no.nav.fo.veilarb.dokument.mappers.IkkeredigerbartDokumentMapper;
import no.nav.fo.veilarb.dokument.mappers.Stubs;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v3.DokumentproduksjonV3;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v3.meldinger.WSProduserDokumentutkastRequest;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v3.meldinger.WSProduserIkkeredigerbartDokumentRequest;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v3.meldinger.WSProduserIkkeredigerbartDokumentResponse;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class DokumentService {

    private DokumentproduksjonV3 dokumentproduksjon;
    private AuthService authService;
    private SakService sakService;

    @Inject
    public DokumentService(DokumentproduksjonV3 dokumentproduksjon,
                           AuthService authService,
                           SakService sakService) {
        this.dokumentproduksjon = dokumentproduksjon;
        this.authService = authService;
        this.sakService = sakService;
    }

    public DokumentbestillingResponsDto bestillDokument(DokumentbestillingDto dto) {
        Bruker bruker = authService.sjekkSkrivetilgangTilBruker(dto.bruker().fnr());

        Dokumentbestilling dokumentbestilling = lagDokumentbestilling(dto, bruker.getAktoerId());
        return produserIkkeredigerbartDokument(dokumentbestilling);
    }

    private Dokumentbestilling lagDokumentbestilling(DokumentbestillingDto dto, String aktorId) {
        Brevdata brevdata = lagBrevdata(dto);
        Sak sak = sakService.finnGjeldendeOppfolgingssak(aktorId);
        String veilederNavn = Stubs.test; // TODO Hent veilederNavn fra veilarbveileder

        return Dokumentbestilling.builder()
                .brevdata(brevdata)
                .sak(sak)
                .veilederNavn(veilederNavn)
                .build();
    }

    private Brevdata lagBrevdata(DokumentbestillingDto dokumentbestilling) {
        return Brevdata.builder()
                .bruker(dokumentbestilling.bruker())
                .mottaker(dokumentbestilling.mottaker())
                .malType(dokumentbestilling.malType())
                .veilederEnhet(dokumentbestilling.veilederEnhet())
                .veilederId(getVeilederId())
                .begrunnelse(dokumentbestilling.begrunnelse())
                .build();
    }

    @SneakyThrows
    private DokumentbestillingResponsDto produserIkkeredigerbartDokument(Dokumentbestilling dokumentbestilling) {
        WSProduserIkkeredigerbartDokumentRequest request =
                IkkeredigerbartDokumentMapper.mapRequest(dokumentbestilling);

        WSProduserIkkeredigerbartDokumentResponse response =
                dokumentproduksjon.produserIkkeredigerbartDokument(request);

        return IkkeredigerbartDokumentMapper.mapRespons(response);
    }

    private String getVeilederId() {
        return SubjectHandler.getSubject()
                .filter(subject -> IdentType.InternBruker.equals(subject.getIdentType()))
                .map(Subject::getUid)
                .orElseThrow(() -> new IllegalStateException("Mangler veileder token"));
    }

    @SneakyThrows
    public byte[] produserDokumentutkast(DokumentbestillingDto dto) {
        authService.sjekkSkrivetilgangTilBruker(dto.bruker().fnr());

        Brevdata brevdata = lagBrevdata(dto);
        WSProduserDokumentutkastRequest dokumentutkastRequest =
                DokumentutkastMapper.produserDokumentutkastRequest(brevdata);

        return dokumentproduksjon.produserDokumentutkast(dokumentutkastRequest).getDokumentutkast();
    }

}
