package no.nav.fo.veilarb.dokument.service;

import lombok.SneakyThrows;
import no.nav.apiapp.feil.Feil;
import no.nav.common.auth.SubjectHandler;
import no.nav.dialogarena.aktor.AktorService;
import no.nav.fo.veilarb.dokument.domain.DokumentBestilling;
import no.nav.fo.veilarb.dokument.domain.JournalpostId;
import no.nav.fo.veilarb.dokument.utils.VeilarbAbacServiceClient;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v3.DokumentproduksjonV3;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v3.informasjon.WSDokumentbestillingsinformasjon;
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

    @Inject
    public DokumentService(AktorService aktorService,
                           DokumentproduksjonV3 dokumentproduksjon,
                           VeilarbAbacServiceClient veilarbAbacServiceClient) {
        this.aktorService = aktorService;
        this.dokumentproduksjon = dokumentproduksjon;
        this.veilarbAbacServiceClient = veilarbAbacServiceClient;
    }

    // TODO
    @SneakyThrows
    public JournalpostId bestillDokument(DokumentBestilling dokumentBestilling) {
        String aktorId = aktorService.getAktorId(dokumentBestilling.fnr())
                .orElseThrow(() -> new Feil(UGYLDIG_REQUEST, "Fant ikke aktør for fnr"));

        validerLesetilgangTilPerson(aktorId);

        WSProduserIkkeredigerbartDokumentRequest request = produserIkkeredigerbartDokumentRequest(dokumentBestilling);
        WSProduserIkkeredigerbartDokumentResponse response = dokumentproduksjon.produserIkkeredigerbartDokument(request);
        return JournalpostId.of(null);
    }

    private void validerLesetilgangTilPerson(String aktorId) {
        SubjectHandler.getSsoToken(OIDC)
                .map(token -> veilarbAbacServiceClient.harLesetilgangTilPerson(token, aktorId))
                .orElseThrow(() -> new Feil(INGEN_TILGANG));
    }

    @SneakyThrows
    private WSProduserIkkeredigerbartDokumentResponse produserIkkeredigerbartDokument(DokumentBestilling dokumentBestilling) {
        WSProduserIkkeredigerbartDokumentRequest request = produserIkkeredigerbartDokumentRequest(dokumentBestilling);
        return dokumentproduksjon.produserIkkeredigerbartDokument(request);

    }

    private WSProduserIkkeredigerbartDokumentRequest produserIkkeredigerbartDokumentRequest(DokumentBestilling dokumentBestilling) {
        WSDokumentbestillingsinformasjon informasjon = produserDokumentbestillingsinformasjon(dokumentBestilling);
        return new WSProduserIkkeredigerbartDokumentRequest()
                .withDokumentbestillingsinformasjon(informasjon);
    }

    private WSDokumentbestillingsinformasjon produserDokumentbestillingsinformasjon(DokumentBestilling dokumentBestilling) {
        WSDokumentbestillingsinformasjon informasjon = new WSDokumentbestillingsinformasjon();

        informasjon.setDokumenttypeId(dokumentBestilling.dokumentTypeId());

        return informasjon;
    }
}
