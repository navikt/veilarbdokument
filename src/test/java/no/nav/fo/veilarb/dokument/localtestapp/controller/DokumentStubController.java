package no.nav.fo.veilarb.dokument.localtestapp.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static no.nav.fo.veilarb.dokument.localtestapp.stub.Values.*;

@RestController
@RequestMapping("/stub/dokument")
public class DokumentStubController {

    @PostMapping
    public String get(@RequestBody String requestBody) {

        if (erProduserDokumentutkastRequest(requestBody)) {
            return produserDokumentutkastResponse(TEST_DOKUMENTUTKAST_STR.getBytes());
        } else if (erPingRequest(requestBody)) {
            return pingResponse();
        } else if (erProduserIkkeredigerbartDokumentRequest(requestBody)) {
            return produserIkkeredigerbartDokumentResponse(TEST_JOURNALPOST_ID, TEST_DOKUMENT_ID);
        }

        throw new RuntimeException("Mangler response mapping");
    }

    private boolean erProduserDokumentutkastRequest(String requestBody) {
        return requestBody.contains("produserDokumentutkastRequest");
    }

    private String produserDokumentutkastResponse(byte[] dokumentutkast) {
        return "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"><soap:Body>" +
                "<ns2:produserDokumentutkastResponse xmlns:ns2=\"http://nav.no/tjeneste/virksomhet/dokumentproduksjon/v3\">" +
                "<response><dokumentutkast>" + dokumentutkast + "</dokumentutkast></response>" +
                "</ns2:produserDokumentutkastResponse>" +
                "</soap:Body></soap:Envelope>";
    }

    private boolean erPingRequest(String requestBody) {
        return requestBody.contains("pingRequest");
    }

    private String pingResponse() {
        return "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"><soap:Body>" +
                "</soap:Body></soap:Envelope>";
    }

    private boolean erProduserIkkeredigerbartDokumentRequest(String requestBody) {
        return requestBody.contains("produserIkkeredigerbartDokument");
    }

    private String produserIkkeredigerbartDokumentResponse(String journapostId, String dokumentId) {
        return "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"><soap:Body>" +
                "<ns2:produserIkkeredigerbartDokumentResponse xmlns:ns2=\"http://nav.no/tjeneste/virksomhet/dokumentproduksjon/v3\">" +
                "<response>" +
                "<journalpostId>" + journapostId + "</journalpostId>" +
                "<dokumentId>" + dokumentId + "</dokumentId>" +
                "</response>" +
                "</ns2:produserIkkeredigerbartDokumentResponse>" +
                "</soap:Body></soap:Envelope>";
    }
}
