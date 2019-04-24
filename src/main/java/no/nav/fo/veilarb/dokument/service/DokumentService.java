package no.nav.fo.veilarb.dokument.service;

import lombok.SneakyThrows;
import no.nav.apiapp.feil.Feil;
import no.nav.brukerdialog.security.domain.IdentType;
import no.nav.common.auth.Subject;
import no.nav.common.auth.SubjectHandler;
import no.nav.dialogarena.aktor.AktorService;
import no.nav.fo.veilarb.dokument.domain.*;
import no.nav.fo.veilarb.dokument.mappers.DokumentutkastMapper;
import no.nav.fo.veilarb.dokument.mappers.Stubs;
import no.nav.fo.veilarb.dokument.utils.VeilarbAbacServiceClient;
import no.nav.fo.veilarb.dokument.mappers.IkkeredigerbartDokumentMapper;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v3.DokumentproduksjonV3;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v3.meldinger.WSProduserDokumentutkastRequest;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v3.meldinger.WSProduserIkkeredigerbartDokumentRequest;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v3.meldinger.WSProduserIkkeredigerbartDokumentResponse;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

import static no.nav.apiapp.feil.FeilType.INGEN_TILGANG;
import static no.nav.apiapp.feil.FeilType.UGYLDIG_REQUEST;
import static no.nav.common.auth.SsoToken.Type.OIDC;

@Component
public class DokumentService {

    private AktorService aktorService;
    private DokumentproduksjonV3 dokumentproduksjon;
    private VeilarbAbacServiceClient veilarbAbacServiceClient;
    private SakService sakService;

    @Inject
    public DokumentService(AktorService aktorService,
                           DokumentproduksjonV3 dokumentproduksjon,
                           VeilarbAbacServiceClient veilarbAbacServiceClient,
                           SakService sakService) {
        this.aktorService = aktorService;
        this.dokumentproduksjon = dokumentproduksjon;
        this.veilarbAbacServiceClient = veilarbAbacServiceClient;
        this.sakService = sakService;
    }

    public DokumentbestillingResponsDto bestillDokument(DokumentbestillingDto dto) {
        String aktorId = aktorService.getAktorId(dto.bruker().fnr())
                .orElseThrow(() -> new Feil(UGYLDIG_REQUEST, "Fant ikke aktør for fnr"));

        // TODO: riktig abac-sjekk
        validerLesetilgangTilPerson(aktorId);

        Dokumentbestilling dokumentbestilling = lagDokumentbestilling(dto, aktorId);
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

    private void validerLesetilgangTilPerson(String aktorId) {
        SubjectHandler.getSsoToken(OIDC)
                .map(token -> veilarbAbacServiceClient.harLesetilgangTilPerson(token, aktorId))
                .orElseThrow(() -> new Feil(INGEN_TILGANG));
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
    public byte[] produserDokumentutkast(DokumentbestillingDto dokumentbestilling) {
        Brevdata brevdata = lagBrevdata(dokumentbestilling);
        WSProduserDokumentutkastRequest dokumentutkastRequest =
                DokumentutkastMapper.produserDokumentutkastRequest(brevdata);

        return dokumentproduksjon.produserDokumentutkast(dokumentutkastRequest).getDokumentutkast();
    }

}
