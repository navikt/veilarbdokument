package no.nav.fo.veilarb.dokument.mappers;

import lombok.SneakyThrows;
import lombok.val;
import no.nav.dok.brevdata.felles.v1.navfelles.*;
import no.nav.dok.brevdata.felles.v1.simpletypes.AktoerType;
import no.nav.dok.veilarbdokmaler._000135.BrevdataType;
import no.nav.dok.veilarbdokmaler._000135.KulepunktListeType;
import no.nav.dok.veilarbdokmaler._000135.KulepunktType;
import no.nav.fo.veilarb.dokument.domain.Brevdata;
import no.nav.fo.veilarb.dokument.domain.MalType;
import org.w3c.dom.*;
import no.nav.dok.veilarbdokmaler.felles.arena_felles.VeilArbNAVFelles;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

public class BrevdataMapper {
    @SneakyThrows
    public static Element mapBrevdata(Brevdata brevdata) {

        return mapBrevdataType(brevdata);
    }

    @SneakyThrows
    private static <T> Marshaller createMarshaller(Class<T> clazz) {
        JAXBContext context = JAXBContext.newInstance(clazz);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        return marshaller;
    }

    @SneakyThrows
    private static <T> Document marshalBrevdata(JAXBElement<T> brevdataElement) {
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Marshaller marshaller = createMarshaller(brevdataElement.getDeclaredType());
        marshaller.marshal(brevdataElement, document);
        return document;
    }

    private static Element mapBrevdataType(Brevdata brevdata) {
        val objectFactory = new no.nav.dok.veilarbdokmaler._000135.ObjectFactory();
        val brevdataType = objectFactory.createBrevdataType();
        val fag = objectFactory.createFagType();
        fag.setFritekstBegrunnelse(brevdata.begrunnelse());

        KulepunktListeType kilder = KulepunktListeType.builder()
                .withKulepunkt(
                        Optional.ofNullable(brevdata.kilder()).orElse(Collections.emptyList()).stream()
                                .map(kilde ->
                                        KulepunktType.builder()
                                                .withKulepunktTekst(kilde)
                                                .build()
                                ).collect(Collectors.toList())
                )
                .build();
        fag.setKulepunktListe(kilder);
        brevdataType.setFag(fag);
        brevdataType.setNAVFelles(mapFelles(brevdata));
//         JAXBElement<BrevdataType> brevdataElement = new JAXBElement<>(new QName("http://nav.no/dok/veilarbdokmaler/000136", "brevdata"), BrevdataType.class, brevdataType);
        JAXBElement<BrevdataType> brevdataElement = objectFactory.createBrevdata(brevdataType);

        Document document = marshalBrevdata(brevdataElement);

//        objectFactory.createBrevdata(null).getName();

        changeNamespaceUri(
                document,
                brevdataElement.getName().getNamespaceURI(),
                utledNamespaceUri(brevdataElement.getName().getNamespaceURI(), brevdata.malType())
        );

        Element documentElement = document.getDocumentElement();

//        fixAttribute(documentElement);

        return documentElement;
    }

    private static String utledNamespaceUri(String templateUri, MalType malType) {
        String pattern = "(.+\\/)(\\d+$)";
        if (templateUri.matches(pattern)) {
            return templateUri.replaceAll(pattern, "$1") + MalType.getMalKode(malType);
        } else {
            throw new IllegalStateException(
                    String.format(
                            "Klarer ikke utlede namespace URI for mal %s (%s) basert på template URI %s",
                            malType.kode,
                            MalType.getMalKode(malType),
                            templateUri));
        }
    }
//
//    private static void fixAttribute(Element element) {
//
//        NamedNodeMap attributes = element.getAttributes();
//
//        for (int i = 0; i < attributes.getLength(); i++) {
//            Node attribute = attributes.item(i);
//            if ("http://nav.no/dok/veilarbdokmaler/000135".equals(attribute.getNodeValue())) {
//                attribute.setNodeValue("http://nav.no/dok/veilarbdokmaler/000136");
//            } else if("http://nav.no/dok/veilarbdokmaler/000136".equals(attribute.getNodeValue()))  {
//                element.removeAttributeNS(attribute.getNamespaceURI(), attribute.getLocalName());
//            }
//        }
//    }

    private static void changeNamespaceUri(Document doc, String fromUri, String toUri) {
        NodeList elements = doc.getElementsByTagName("*");
        for (int i = 0; i < elements.getLength(); i++) {
            Node element = elements.item(i);
            System.out.println(element.getNodeName());
            System.out.println(element.getNamespaceURI());
            if (fromUri.equals(element.getNamespaceURI())) {
                doc.renameNode(element, toUri, element.getNodeName());
            }
        }
    }

//    public static JAXBElement<BrevdataType> createBrevdata(BrevdataType value) {
//        QName qName = new QName("http://nav.no/dok/veilarbdokmaler/000136", "brevdata");
//        return new JAXBElement<>(qName, BrevdataType.class, value);
//    }

    private static VeilArbNAVFelles mapFelles(Brevdata brevdata) {

        return VeilArbNAVFelles.builder()
                .withBehandlendeEnhet(mapBehandlendeEnhet(brevdata.veilederEnhet()))
                .withKontaktinformasjon(mapKontaktinformasjon(brevdata.veilederEnhet()))
                .withMottaker(mapMottaker(brevdata.mottaker().fnr()))
                .withSakspart(mapSakspart(brevdata.bruker().fnr()))
                .withSignerendeBeslutter(mapSaksbehandler(brevdata))
                .withSignerendeSaksbehandler(mapSaksbehandler(brevdata))
                .build();

    }


    private static NavEnhet mapBehandlendeEnhet(String enhet) {
        return NavEnhet.builder()
                .withEnhetsId(enhet)
                .build();
    }

    private static Kontaktinformasjon mapKontaktinformasjon(String enhet) {
        return Kontaktinformasjon.builder()
                .withBesoksadresse(
                        Besoksadresse.builder()
                                .withEnhetsId(enhet)
                                .build())
                .withReturadresse(
                        Returadresse.builder()
                                .withEnhetsId(enhet)
                                .build())
                .withPostadresse(
                        Postadresse.builder()
                                .withEnhetsId(enhet)
                                .build())
                .build();
    }

    private static Person mapMottaker(String id) {
        return Person.builder()
                .withId(id)
                .withTypeKode(AktoerType.PERSON)
                .build();
    }

    private static Sakspart mapSakspart(String id) {
        return Sakspart.builder()
                .withId(id)
                .withTypeKode(AktoerType.PERSON)
                .build();
    }

    private static Saksbehandler mapSaksbehandler(Brevdata brevdata) {
        return Saksbehandler.builder()
                .withNavAnsatt(
                        NavAnsatt.builder()
                                .withBerik(false)
                                .withAnsattId(brevdata.veilederId())
                                .withNavn(brevdata.veilederNavn())
                                .build())
                .withNavEnhet(
                        NavEnhet.builder()
                                .withEnhetsId(brevdata.veilederEnhet())
                                .build())
                .build();
    }
}
