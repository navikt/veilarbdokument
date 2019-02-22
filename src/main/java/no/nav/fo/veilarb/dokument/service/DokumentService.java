package no.nav.fo.veilarb.dokument.service;

import lombok.SneakyThrows;
import no.nav.dialogarena.aktor.AktorService;
import no.nav.fo.veilarb.dokument.domain.DokumentBestilling;
import no.nav.fo.veilarb.dokument.domain.JournalpostId;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v3.*;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v3.informasjon.WSDokumentbestillingsinformasjon;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v3.meldinger.WSProduserIkkeredigerbartDokumentRequest;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v3.meldinger.WSProduserIkkeredigerbartDokumentResponse;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Optional;

@Component
public class DokumentService {

    private AktorService aktorService;
    private DokumentproduksjonV3 dokumentproduksjon;

    @Inject
    public DokumentService(AktorService aktorService, DokumentproduksjonV3 dokumentproduksjon) {
        this.aktorService = aktorService;
        this.dokumentproduksjon = dokumentproduksjon;
    }

    // TODO
    @SneakyThrows
    public JournalpostId bestillDokument(DokumentBestilling dokumentBestilling) {
        Optional<String> aktorId = aktorService.getAktorId(dokumentBestilling.fnr());
        WSProduserIkkeredigerbartDokumentRequest request = produserIkkeredigerbartDokumentRequest(dokumentBestilling);
        // WSProduserIkkeredigerbartDokumentResponse response = dokumentproduksjon.produserIkkeredigerbartDokument(request);
        return JournalpostId.of(null);
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
