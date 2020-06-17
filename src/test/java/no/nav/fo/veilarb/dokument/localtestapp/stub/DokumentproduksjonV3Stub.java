package no.nav.fo.veilarb.dokument.localtestapp.stub;

import no.nav.tjeneste.virksomhet.dokumentproduksjon.v3.*;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v3.meldinger.*;

import static no.nav.fo.veilarb.dokument.localtestapp.stub.Values.TEST_DOKUMENT_ID;
import static no.nav.fo.veilarb.dokument.localtestapp.stub.Values.TEST_JOURNALPOST_ID;

public class DokumentproduksjonV3Stub implements DokumentproduksjonV3 {

    @Override
    public void ping() {

    }

    @Override
    public void avbrytVedlegg(WSAvbrytVedleggRequest request) {

    }

    @Override
    public WSProduserDokumentutkastResponse produserDokumentutkast(WSProduserDokumentutkastRequest request) {
        WSProduserDokumentutkastResponse response = new WSProduserDokumentutkastResponse();
        response.setDokumentutkast("dokumentutkast".getBytes());
        return response;
    }

    @Override
    public void knyttVedleggTilForsendelse(WSKnyttVedleggTilForsendelseRequest request) {

    }

    @Override
    public WSProduserIkkeredigerbartDokumentResponse produserIkkeredigerbartDokument(WSProduserIkkeredigerbartDokumentRequest request) {
        WSProduserIkkeredigerbartDokumentResponse response = new WSProduserIkkeredigerbartDokumentResponse();
        response.setJournalpostId(TEST_JOURNALPOST_ID);
        response.setDokumentId(TEST_DOKUMENT_ID);
        return response;
    }

    @Override
    public WSProduserRedigerbartDokumentResponse produserRedigerbartDokument(WSProduserRedigerbartDokumentRequest request) {
        return null;
    }

    @Override
    public void avbrytForsendelse(WSAvbrytForsendelseRequest request) {

    }

    @Override
    public void ferdigstillForsendelse(WSFerdigstillForsendelseRequest request) {

    }

    @Override
    public WSProduserIkkeredigerbartVedleggResponse produserIkkeredigerbartVedlegg(WSProduserIkkeredigerbartVedleggRequest request) {
        return null;
    }

    @Override
    public WSRedigerDokumentResponse redigerDokument(WSRedigerDokumentRequest request) {
        return null;
    }

    @Override
    public void endreDokumentTilRedigerbart(WSEndreDokumentTilRedigerbartRequest request) {

    }
}
