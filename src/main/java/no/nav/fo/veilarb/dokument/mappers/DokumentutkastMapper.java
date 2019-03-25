package no.nav.fo.veilarb.dokument.mappers;

import no.nav.fo.veilarb.dokument.domain.Dokumentbestilling;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v3.meldinger.WSProduserDokumentutkastRequest;

public class DokumentutkastMapper {

    public static WSProduserDokumentutkastRequest produserDokumentutkastRequest(Dokumentbestilling dokumentbestilling) {
        Object brevdata = BrevdataMapper.mapBrevdata(dokumentbestilling);

        return new WSProduserDokumentutkastRequest()
                .withDokumenttypeId(dokumentbestilling.dokumenttypeId())
                .withAny(brevdata);
    }
}
