package no.nav.fo.veilarb.dokument.service;

import lombok.SneakyThrows;
import no.nav.apiapp.feil.Feil;
import no.nav.common.auth.SubjectHandler;
import no.nav.dialogarena.aktor.AktorService;
import no.nav.fo.veilarb.dokument.domain.Dokumentbestilling;
import no.nav.fo.veilarb.dokument.domain.DokumentbestillingRespons;
import no.nav.fo.veilarb.dokument.domain.Sak;
import no.nav.fo.veilarb.dokument.mappers.DokumentutkastMapper;
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

    public DokumentbestillingRespons bestillDokument(Dokumentbestilling dokumentbestilling) {
        String aktorId = aktorService.getAktorId(dokumentbestilling.bruker().fnr())
                .orElseThrow(() -> new Feil(UGYLDIG_REQUEST, "Fant ikke aktør for fnr"));

        // TODO: riktig abac-sjekk
        validerLesetilgangTilPerson(aktorId);

        // TODO bruk sak i request til dokprod
        Sak sak = sakService.finnGjeldendeOppfolgingssak(aktorId);

        DokumentbestillingRespons respons = produserIkkeredigerbartDokument(dokumentbestilling);

        return respons;
    }

    private void validerLesetilgangTilPerson(String aktorId) {
        SubjectHandler.getSsoToken(OIDC)
                .map(token -> veilarbAbacServiceClient.harLesetilgangTilPerson(token, aktorId))
                .orElseThrow(() -> new Feil(INGEN_TILGANG));
    }

    @SneakyThrows
    private DokumentbestillingRespons produserIkkeredigerbartDokument(Dokumentbestilling dokumentbestilling) {
        WSProduserIkkeredigerbartDokumentRequest request = IkkeredigerbartDokumentMapper.mapRequest(dokumentbestilling);

        WSProduserIkkeredigerbartDokumentResponse response = dokumentproduksjon.produserIkkeredigerbartDokument(request);

        return IkkeredigerbartDokumentMapper.mapRespons(response);
    }

    @SneakyThrows
    public byte[] produserDokumentutkast(Dokumentbestilling dokumentbestilling) {
        WSProduserDokumentutkastRequest dokumentutkastRequest = DokumentutkastMapper.produserDokumentutkastRequest(dokumentbestilling);

        return dokumentproduksjon.produserDokumentutkast(dokumentutkastRequest).getDokumentutkast();
    }

}
