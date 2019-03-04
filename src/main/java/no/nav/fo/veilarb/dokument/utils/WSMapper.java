package no.nav.fo.veilarb.dokument.utils;

import no.nav.fo.veilarb.dokument.domain.DokumentBestilling;
import no.nav.fo.veilarb.dokument.domain.Person;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v3.informasjon.WSDokumentbestillingsinformasjon;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v3.informasjon.WSFagomraader;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v3.informasjon.WSFagsystemer;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v3.informasjon.WSPerson;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v3.meldinger.WSProduserIkkeredigerbartDokumentRequest;

public class WSMapper {

    private static String BESTILLENDE_FAGSYSTEM_KODE = "AO01";
    private static String SAKSTILHORENDE_FAGSYSTEM_KODE = "FS22";
    private static String DOKUMENTTILHORENDE_FAGOMRAADE = "OPP";

    public static WSProduserIkkeredigerbartDokumentRequest produserIkkeredigerbartDokumentRequest(DokumentBestilling dokumentBestilling) {
        WSDokumentbestillingsinformasjon informasjon =
                WSMapper.dokumentbestillingsinformasjon(dokumentBestilling);

        return new WSProduserIkkeredigerbartDokumentRequest()
                .withDokumentbestillingsinformasjon(informasjon);
    }

    public static WSDokumentbestillingsinformasjon dokumentbestillingsinformasjon(DokumentBestilling dokumentBestilling) {
        WSDokumentbestillingsinformasjon informasjon = new WSDokumentbestillingsinformasjon();

        informasjon.setDokumenttypeId(dokumentBestilling.dokumentTypeId());


        informasjon.setBestillendeFagsystem(new WSFagsystemer().withValue(BESTILLENDE_FAGSYSTEM_KODE));
        informasjon.setSakstilhoerendeFagsystem(new WSFagsystemer().withValue(SAKSTILHORENDE_FAGSYSTEM_KODE));
        informasjon.setDokumenttilhoerendeFagomraade(new WSFagomraader().withValue(DOKUMENTTILHORENDE_FAGOMRAADE));
        informasjon.setBruker(person(dokumentBestilling.bruker()));
        informasjon.setMottaker(person(dokumentBestilling.mottaker()));
        informasjon.setJournalsakId(dokumentBestilling.journalsakId());

        return informasjon;
    }

    public static WSPerson person(Person person) {
        WSPerson wsPerson = new WSPerson();
        wsPerson.setIdent(person.fnr());
        wsPerson.setNavn(person.navn());
        return wsPerson;
    }
}
