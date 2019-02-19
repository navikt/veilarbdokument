package no.nav.fo.veilarb.dokument.service;

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
    public JournalpostId bestillDokument(DokumentBestilling dokumentBestilling) {
        Optional<String> aktorId = aktorService.getAktorId(dokumentBestilling.fnr());
        WSProduserIkkeredigerbartDokumentRequest request = produserIkkeredigerbartDokumentRequest();
//        WSProduserIkkeredigerbartDokumentResponse response = dokumentproduksjon.produserIkkeredigerbartDokument(request);
        return JournalpostId.of(null);
    }

    private WSProduserIkkeredigerbartDokumentResponse produserIkkeredigerbartDokument() throws ProduserIkkeredigerbartDokumentDokumentErRedigerbart, ProduserIkkeRedigerbartDokumentJoarkForretningsmessigUnntak, ProduserIkkeredigerbartDokumentSikkerhetsbegrensning, ProduserIkkeredigerbartDokumentBrevdataValideringFeilet, ProduserIkkeredigerbartDokumentDokumentErVedlegg, ProduserIkkeRedigerbartDokumentInputValideringFeilet {
        WSProduserIkkeredigerbartDokumentRequest request = produserIkkeredigerbartDokumentRequest();
        return dokumentproduksjon.produserIkkeredigerbartDokument(request);

    }

    private WSProduserIkkeredigerbartDokumentRequest produserIkkeredigerbartDokumentRequest() {
        WSDokumentbestillingsinformasjon informasjon = new WSDokumentbestillingsinformasjon();
        return new WSProduserIkkeredigerbartDokumentRequest()
                .withDokumentbestillingsinformasjon(informasjon);
    }
}
