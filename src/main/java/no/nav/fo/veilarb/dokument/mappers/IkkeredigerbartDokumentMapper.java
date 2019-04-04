package no.nav.fo.veilarb.dokument.mappers;

import lombok.SneakyThrows;
import no.nav.fo.veilarb.dokument.domain.Adresse;
import no.nav.fo.veilarb.dokument.domain.Dokumentbestilling;
import no.nav.fo.veilarb.dokument.domain.DokumentbestillingRespons;
import no.nav.fo.veilarb.dokument.domain.Person;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v3.informasjon.*;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v3.meldinger.WSProduserIkkeredigerbartDokumentRequest;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v3.meldinger.WSProduserIkkeredigerbartDokumentResponse;

public class IkkeredigerbartDokumentMapper {

    private static String BESTILLENDE_FAGSYSTEM_KODE = "AO01";
    private static String SAKSTILHORENDE_FAGSYSTEM_KODE = "FS22";
    private static String DOKUMENTTILHORENDE_FAGOMRAADE = "OPP";

    @SneakyThrows
    public static WSProduserIkkeredigerbartDokumentRequest mapRequest(Dokumentbestilling dokumentbestilling) {

        WSDokumentbestillingsinformasjon informasjon =
                MapDokumentbestillingsinformasjon(dokumentbestilling);

        return new WSProduserIkkeredigerbartDokumentRequest()
                .withDokumentbestillingsinformasjon(informasjon)
                .withAny(BrevdataMapper.mapBrevdata(dokumentbestilling));
    }

    public static WSDokumentbestillingsinformasjon MapDokumentbestillingsinformasjon(Dokumentbestilling dokumentBestilling) {
        WSDokumentbestillingsinformasjon informasjon = new WSDokumentbestillingsinformasjon();

        informasjon.setDokumenttypeId(dokumentBestilling.dokumenttypeId());


        informasjon.setBestillendeFagsystem(new WSFagsystemer().withValue(BESTILLENDE_FAGSYSTEM_KODE));
        informasjon.setSakstilhoerendeFagsystem(new WSFagsystemer().withValue(SAKSTILHORENDE_FAGSYSTEM_KODE));
        informasjon.setDokumenttilhoerendeFagomraade(new WSFagomraader().withValue(DOKUMENTTILHORENDE_FAGOMRAADE));
        informasjon.setBruker(mapPerson(dokumentBestilling.bruker()));
        informasjon.setMottaker(mapPerson(dokumentBestilling.mottaker()));
        informasjon.setJournalsakId(dokumentBestilling.journalsakId());
        informasjon.setJournalfoerendeEnhet(dokumentBestilling.journalforendeEnhet());
        informasjon.setUtledRegisterInfo(true);

        // TODO: følgende felter er ikke spesifisert hvilke verdier som er riktig
        if (dokumentBestilling.adresse() != null) {
            informasjon.setAdresse(mapAdresse(dokumentBestilling.adresse()));
        }
        informasjon.setInkludererEksterneVedlegg(false);
        informasjon.setFerdigstillForsendelse(false);
        informasjon.setSaksbehandlernavn(Stubs.test); // TODO: slå opp navn på saksbehandler?

        return informasjon;
    }

    public static WSAdresse mapAdresse(Adresse adresse) {
        return new WSNorskPostadresse()
                .withAdresselinje1(adresse.adresselinje1())
                .withAdresselinje2(adresse.adresselinje2())
                .withAdresselinje3(adresse.adresselinje3())
                .withLand(new WSLandkoder().withValue(adresse.land()))
                .withPostnummer(adresse.postnummer())
                .withPoststed(adresse.poststed());
    }

    public static WSPerson mapPerson(Person person) {
        WSPerson wsPerson = new WSPerson();
        wsPerson.setIdent(person.fnr());
        wsPerson.setNavn(person.navn());
        return wsPerson;
    }

    public static DokumentbestillingRespons mapRespons(WSProduserIkkeredigerbartDokumentResponse response) {
        return DokumentbestillingRespons.of(
                response.getJournalpostId(),
                response.getDokumentId());
    }
}