package no.nav.fo.veilarb.dokument.utils;

import lombok.SneakyThrows;
import no.nav.dok.metaforcemal.*;
import no.nav.fo.veilarb.dokument.domain.*;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v3.informasjon.*;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v3.meldinger.WSProduserDokumentutkastRequest;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v3.meldinger.WSProduserIkkeredigerbartDokumentRequest;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v3.meldinger.WSProduserIkkeredigerbartDokumentResponse;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilderFactory;

public class WSMapper {

    private static String BESTILLENDE_FAGSYSTEM_KODE = "AO01";
    private static String SAKSTILHORENDE_FAGSYSTEM_KODE = "FS22";
    private static String DOKUMENTTILHORENDE_FAGOMRAADE = "OPP";

    @SneakyThrows
    public static WSProduserIkkeredigerbartDokumentRequest produserIkkeredigerbartDokumentRequest(Dokumentbestilling dokumentbestilling) {

        WSDokumentbestillingsinformasjon informasjon =
                WSMapper.dokumentbestillingsinformasjon(dokumentbestilling);

        return new WSProduserIkkeredigerbartDokumentRequest()
                .withDokumentbestillingsinformasjon(informasjon)
                .withAny(brevdata(dokumentbestilling));
    }

    @SneakyThrows
    private static Object brevdata(Dokumentbestilling dokumentbestilling) {

        JAXBContext context = JAXBContext.newInstance(BrevdataType.class);

        JAXBElement<BrevdataType> brevdata = new no.nav.dok.metaforcemal.ObjectFactory().createBrevdata(new BrevdataType());
        FellesType fellesType = new FellesType();
        fellesType.setSpraakkode("NB");
        SignerendeSaksbehandlerType signerendeSaksbehandlerType = new SignerendeSaksbehandlerType();
        signerendeSaksbehandlerType.setSignerendeSaksbehandlerNavn("Test");
        fellesType.setSignerendeSaksbehandler(signerendeSaksbehandlerType);
        SakspartType sakpartType = new SakspartType();
        sakpartType.setSakspartId(Long.parseLong(dokumentbestilling.bruker().fnr()));
        sakpartType.setSakspartNavn(dokumentbestilling.bruker().navn());
        sakpartType.setSakspartTypeKode(SakspartTypeKode.PERSON);
        fellesType.setSakspart(sakpartType);
        MottakerType mottakerType = new MottakerType();
        mottakerType.setMottakerId(Long.parseLong(dokumentbestilling.mottaker().fnr()));
        mottakerType.setMottakerNavn(dokumentbestilling.bruker().navn());
        mottakerType.setMottakerTypeKode(MottakerTypeKode.PERSON);
        MottakerAdresseType mottakerAdresseType = new MottakerAdresseType();
        mottakerAdresseType.setAdresselinje1(dokumentbestilling.adresse().adresselinje1());
        mottakerAdresseType.setAdresselinje2(dokumentbestilling.adresse().adresselinje2());
        mottakerAdresseType.setAdresselinje3(dokumentbestilling.adresse().adresselinje3());
        mottakerAdresseType.setLand(dokumentbestilling.adresse().land());
        mottakerAdresseType.setPostNr(Short.parseShort(dokumentbestilling.adresse().postnummer()));
        mottakerAdresseType.setPoststed(dokumentbestilling.adresse().poststed());
        mottakerType.setMottakerAdresse(mottakerAdresseType);
        fellesType.setMottaker(mottakerType);
        fellesType.setNavnAvsenderEnhet("Test");
        KontaktInformasjonType kontaktInformasjonType = new KontaktInformasjonType();
        BesoksadresseType besoksadresseType = new BesoksadresseType();
        besoksadresseType.setAdresselinje("Test");
        besoksadresseType.setPostNr("0118");
        besoksadresseType.setPoststed("Test");
        kontaktInformasjonType.setBesoksadresse(besoksadresseType);

        PostadresseType postadresseType = new PostadresseType();
        postadresseType.setAdresselinje("Test");
        postadresseType.setNavEnhetsNavn("Test");
        postadresseType.setPostNr((short)118);
        postadresseType.setPoststed("Test");
        kontaktInformasjonType.setPostadresse(postadresseType);

        ReturadresseType returadresseType = new ReturadresseType();
        returadresseType.setAdresselinje("Test");
        returadresseType.setNavEnhetsNavn("Test");
        returadresseType.setPostNr((short)118);
        returadresseType.setPoststed("Test");
        kontaktInformasjonType.setReturadresse(returadresseType);

        kontaktInformasjonType.setKontaktTelefonnummer("0");
        fellesType.setKontaktInformasjon(kontaktInformasjonType);
        brevdata.getValue().setFelles(fellesType);
        brevdata.getValue().setFag(new FagType());


        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

        marshaller.marshal(brevdata, document);
        Element documentElement = document.getDocumentElement();

        return documentElement;
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

    public static DokumentbestillingRespons produserIkkeredigerbartDokumentResponse(WSProduserIkkeredigerbartDokumentResponse response) {
        return DokumentbestillingRespons.of(
                response.getJournalpostId(),
                response.getDokumentId());
    }
}
