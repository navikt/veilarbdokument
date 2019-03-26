package no.nav.fo.veilarb.dokument.mappers;

import lombok.SneakyThrows;
import no.nav.dok.metaforcemal.*;
import no.nav.fo.veilarb.dokument.domain.Dokumentbestilling;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilderFactory;

public class BrevdataMapper {

    @SneakyThrows
    public static Element mapBrevdata(Dokumentbestilling dokumentbestilling) {
        return marshalBrevdata(mapBrevdataType(dokumentbestilling));
    }

    @SneakyThrows
    private static Element marshalBrevdata(BrevdataType brevdata) {
        JAXBContext context = JAXBContext.newInstance(BrevdataType.class);

        JAXBElement<BrevdataType> brevdataElement =
                new no.nav.dok.metaforcemal.ObjectFactory().createBrevdata(brevdata);

        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

        marshaller.marshal(brevdataElement, document);

        Element documentElement = document.getDocumentElement();

        return documentElement;
    }

    private static BrevdataType mapBrevdataType(Dokumentbestilling dokumentbestilling) {
        BrevdataType brevdataType = new BrevdataType();
        brevdataType.setFelles(mapFellesType(dokumentbestilling));
        brevdataType.setFag(new FagType());
        return brevdataType;
    }

    private static FellesType mapFellesType(Dokumentbestilling dokumentbestilling) {
        FellesType fellesType = new FellesType();

        fellesType.setSpraakkode("NB");
        fellesType.setSignerendeSaksbehandler(mapSignerendeSaksbehandlerType("Test"));
        fellesType.setSakspart(mapSakspartType(dokumentbestilling));
        fellesType.setMottaker(mapMottakerType(dokumentbestilling));
        fellesType.setNavnAvsenderEnhet("Test");
        fellesType.setKontaktInformasjon(mapKontaktInformasjonType());

        return fellesType;
    }

    private static SignerendeSaksbehandlerType mapSignerendeSaksbehandlerType(String saksbehandlerNavn) {
        SignerendeSaksbehandlerType signerendeSaksbehandlerType = new SignerendeSaksbehandlerType();
        signerendeSaksbehandlerType.setSignerendeSaksbehandlerNavn(saksbehandlerNavn);
        return signerendeSaksbehandlerType;
    }

    private static SakspartType mapSakspartType(Dokumentbestilling dokumentbestilling) {
        SakspartType sakpartType = new SakspartType();
        sakpartType.setSakspartId(Long.parseLong(dokumentbestilling.bruker().fnr()));
        sakpartType.setSakspartNavn(dokumentbestilling.bruker().navn());
        sakpartType.setSakspartTypeKode(SakspartTypeKode.PERSON);
        return sakpartType;
    }

    private static MottakerType mapMottakerType(Dokumentbestilling dokumentbestilling) {
        MottakerType mottakerType = new MottakerType();
        mottakerType.setMottakerId(Long.parseLong(dokumentbestilling.mottaker().fnr()));
        mottakerType.setMottakerNavn(dokumentbestilling.bruker().navn());
        mottakerType.setMottakerTypeKode(MottakerTypeKode.PERSON);

        mottakerType.setMottakerAdresse(mapMottakerAdresseType(dokumentbestilling));
        return mottakerType;
    }

    private static MottakerAdresseType mapMottakerAdresseType(Dokumentbestilling dokumentbestilling) {
        MottakerAdresseType mottakerAdresseType = new MottakerAdresseType();
        mottakerAdresseType.setAdresselinje1(dokumentbestilling.adresse().adresselinje1());
        mottakerAdresseType.setAdresselinje2(dokumentbestilling.adresse().adresselinje2());
        mottakerAdresseType.setAdresselinje3(dokumentbestilling.adresse().adresselinje3());
        mottakerAdresseType.setLand(dokumentbestilling.adresse().land());
        mottakerAdresseType.setPostNr(Short.parseShort(dokumentbestilling.adresse().postnummer()));
        mottakerAdresseType.setPoststed(dokumentbestilling.adresse().poststed());
        return mottakerAdresseType;
    }

    private static KontaktInformasjonType mapKontaktInformasjonType() {
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

        return kontaktInformasjonType;
    }
}
