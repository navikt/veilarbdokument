package no.nav.fo.veilarb.dokument.utils;

import no.nav.fo.veilarb.dokument.domain.Adresse;
import no.nav.fo.veilarb.dokument.domain.Dokumentbestilling;
import no.nav.fo.veilarb.dokument.domain.Person;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v3.informasjon.*;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v3.meldinger.WSProduserDokumentutkastRequest;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v3.meldinger.WSProduserIkkeredigerbartDokumentRequest;

public class WSMapper {

    private static String BESTILLENDE_FAGSYSTEM_KODE = "AO01";
    private static String SAKSTILHORENDE_FAGSYSTEM_KODE = "FS22";
    private static String DOKUMENTTILHORENDE_FAGOMRAADE = "OPP";

    public static WSProduserIkkeredigerbartDokumentRequest produserIkkeredigerbartDokumentRequest(Dokumentbestilling dokumentBestilling) {
        WSDokumentbestillingsinformasjon informasjon =
                WSMapper.dokumentbestillingsinformasjon(dokumentBestilling);

        return new WSProduserIkkeredigerbartDokumentRequest()
                .withDokumentbestillingsinformasjon(informasjon);
    }

    public static WSDokumentbestillingsinformasjon dokumentbestillingsinformasjon(Dokumentbestilling dokumentBestilling) {
        WSDokumentbestillingsinformasjon informasjon = new WSDokumentbestillingsinformasjon();

        informasjon.setDokumenttypeId(dokumentBestilling.dokumenttypeId());


        informasjon.setBestillendeFagsystem(new WSFagsystemer().withValue(BESTILLENDE_FAGSYSTEM_KODE));
        informasjon.setSakstilhoerendeFagsystem(new WSFagsystemer().withValue(SAKSTILHORENDE_FAGSYSTEM_KODE));
        informasjon.setDokumenttilhoerendeFagomraade(new WSFagomraader().withValue(DOKUMENTTILHORENDE_FAGOMRAADE));
        informasjon.setBruker(person(dokumentBestilling.bruker()));
        informasjon.setMottaker(person(dokumentBestilling.mottaker()));
        informasjon.setJournalsakId(dokumentBestilling.journalsakId());
        informasjon.setJournalfoerendeEnhet(dokumentBestilling.journalforendeEnhet());
        informasjon.setUtledRegisterInfo(true);

        // TODO: følgende felter er ikke spesifisert hvilke verdier som er riktig
        if (dokumentBestilling.adresse() != null) {
            informasjon.setAdresse(adresse(dokumentBestilling.adresse()));
        }
        informasjon.setInkludererEksterneVedlegg(false);
        informasjon.setFerdigstillForsendelse(false);
        informasjon.setSaksbehandlernavn("Test Navn"); // TODO: slå opp navn på saksbehandler?

        return informasjon;
    }

    public static WSAdresse adresse(Adresse adresse) {
        return new WSNorskPostadresse()
                .withAdresselinje1(adresse.adresselinje1())
                .withAdresselinje2(adresse.adresselinje2())
                .withAdresselinje3(adresse.adresselinje3())
                .withLand(new WSLandkoder().withValue(adresse.land()))
                .withPostnummer(adresse.postnummer())
                .withPoststed(adresse.poststed());
    }

    public static WSPerson person(Person person) {
        WSPerson wsPerson = new WSPerson();
        wsPerson.setIdent(person.fnr());
        wsPerson.setNavn(person.navn());
        return wsPerson;
    }

    public static WSProduserDokumentutkastRequest produserDokumentutkastRequest(Dokumentbestilling dokumentbestilling) {
        WSDokumentbestillingsinformasjon dokumentbestillingsinformasjon =
                dokumentbestillingsinformasjon(dokumentbestilling);

        return new WSProduserDokumentutkastRequest()
                .withDokumenttypeId(dokumentbestilling.dokumenttypeId())
                .withAny(dokumentbestillingsinformasjon);
    }
}
