package no.nav.fo.veilarb.dokument.mappers;

import no.nav.fo.veilarb.dokument.domain.Brevdata;
import no.nav.fo.veilarb.dokument.domain.MalType;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v3.meldinger.WSProduserDokumentutkastRequest;

public class DokumentutkastMapper {

    public static WSProduserDokumentutkastRequest produserDokumentutkastRequest(Brevdata brevdata) {
        return new WSProduserDokumentutkastRequest()
                .withDokumenttypeId(MalType.getMalKode(brevdata.malType()))
                .withAny(BrevdataMapper.mapBrevdata(brevdata))
                .withUtledRegisterInfo(true);
    }
}
