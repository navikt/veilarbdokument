package no.nav.fo.veilarb.dokument.mappers;

import lombok.SneakyThrows;
import no.nav.fo.veilarb.dokument.domain.*;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v3.informasjon.*;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v3.meldinger.WSProduserIkkeredigerbartDokumentRequest;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v3.meldinger.WSProduserIkkeredigerbartDokumentResponse;
import org.w3c.dom.Element;

public class IkkeredigerbartDokumentMapper {

    private static String BESTILLENDE_FAGSYSTEM_KODE = "AO01";
    private static String SAKSTILHORENDE_FAGSYSTEM_KODE = "FS22";
    private static String DOKUMENTTILHORENDE_FAGOMRAADE = "OPP";

    @SneakyThrows
    public static WSProduserIkkeredigerbartDokumentRequest mapRequest(Dokumentbestilling dokumentbestilling) {

        WSDokumentbestillingsinformasjon bestillingsinformasjon =
                mapDokumentbestillingsinformasjon(dokumentbestilling);

        Element brevdata = BrevdataMapper.mapBrevdata(dokumentbestilling.brevdata());

        return new WSProduserIkkeredigerbartDokumentRequest()
                .withDokumentbestillingsinformasjon(bestillingsinformasjon)
                .withAny(brevdata);
    }

    public static WSDokumentbestillingsinformasjon mapDokumentbestillingsinformasjon(
            Dokumentbestilling dokumentbestilling) {

        Brevdata brevdata = dokumentbestilling.brevdata();

        WSDokumentbestillingsinformasjon informasjon = new WSDokumentbestillingsinformasjon();

        informasjon.setDokumenttypeId(MalType.getMalKode(brevdata.malType()));

        informasjon.setBestillendeFagsystem(new WSFagsystemer().withValue(BESTILLENDE_FAGSYSTEM_KODE));
        informasjon.setSakstilhoerendeFagsystem(new WSFagsystemer().withValue(SAKSTILHORENDE_FAGSYSTEM_KODE));
        informasjon.setDokumenttilhoerendeFagomraade(new WSFagomraader().withValue(DOKUMENTTILHORENDE_FAGOMRAADE));
        informasjon.setBruker(mapPerson(brevdata.bruker()));
        informasjon.setMottaker(mapPerson(brevdata.mottaker()));
        informasjon.setJournalsakId(Integer.toString(dokumentbestilling.sak().id()));
        informasjon.setJournalfoerendeEnhet(brevdata.veilederEnhet());
        informasjon.setUtledRegisterInfo(true);

        informasjon.setInkludererEksterneVedlegg(false);
        informasjon.setFerdigstillForsendelse(true);
        informasjon.setSaksbehandlernavn(dokumentbestilling.veilederNavn());

        return informasjon;
    }

    public static WSPerson mapPerson(Person person) {
        WSPerson wsPerson = new WSPerson();
        wsPerson.setIdent(person.fnr());
        return wsPerson;
    }

    public static DokumentbestillingResponsDto mapRespons(WSProduserIkkeredigerbartDokumentResponse response) {
        return DokumentbestillingResponsDto.of(
                response.getJournalpostId(),
                response.getDokumentId());
    }
}
