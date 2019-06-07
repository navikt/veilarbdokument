package no.nav.fo.veilarb.dokument.mappers;

import lombok.SneakyThrows;
import lombok.val;
import no.nav.dok.brevdata.felles.v1.navfelles.*;
import no.nav.dok.brevdata.felles.v1.simpletypes.AktoerType;
import no.nav.dok.veilarbdokmaler._000135.BrevdataType;
import no.nav.dok.veilarbdokmaler._000135.FagType;
import no.nav.dok.veilarbdokmaler._000135.KulepunktListeType;
import no.nav.dok.veilarbdokmaler._000135.KulepunktType;
import no.nav.dok.veilarbdokmaler.felles.arena_felles.VeilArbNAVFelles;
import no.nav.fo.veilarb.dokument.domain.Brevdata;
import no.nav.fo.veilarb.dokument.domain.MalType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BrevdataMapper {
    @SneakyThrows
    public static Element mapBrevdata(Brevdata brevdata) {

        BrevdataType brevdataType = mapBrevdataType(brevdata);

        return marshalBrevdata(brevdataType, brevdata.malType());
    }

    @SneakyThrows
    private static <T> Marshaller createMarshaller(Class<T> clazz) {
        JAXBContext context = JAXBContext.newInstance(clazz);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        return marshaller;
    }

    @SneakyThrows
    private static Element marshalBrevdata(BrevdataType brevdata, MalType malType) {
        val objectFactory = new no.nav.dok.veilarbdokmaler._000135.ObjectFactory();
        JAXBElement<BrevdataType> brevdataElement = objectFactory.createBrevdata(brevdata);
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Marshaller marshaller = createMarshaller(brevdataElement.getDeclaredType());
        marshaller.marshal(brevdataElement, document);

        changeNamespaceUri(
                document,
                brevdataElement.getName().getNamespaceURI(),
                utledNamespaceUri(brevdataElement.getName().getNamespaceURI(), malType)
        );

        return document.getDocumentElement();
    }


    private static BrevdataType mapBrevdataType(Brevdata brevdata) {

        val brevdataType = new BrevdataType();
        val fag = new FagType();
        fag.setFritekstBegrunnelse(brevdata.begrunnelse());

        KulepunktListeType kilder = mapKilder(brevdata.kilder());
        fag.setKulepunktListe(kilder);
        brevdataType.setFag(fag);
        brevdataType.setNAVFelles(mapFelles(brevdata));

        return brevdataType;
    }


    private static KulepunktListeType mapKilder(List<String> kilder) {
        return KulepunktListeType.builder()
                .withKulepunkt(
                        Optional.ofNullable(kilder).orElse(Collections.emptyList()).stream()
                                .map(kilde ->
                                        KulepunktType.builder()
                                                .withKulepunktTekst(kilde)
                                                .build()
                                ).collect(Collectors.toList())
                )
                .build();
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

    private static void changeNamespaceUri(Document doc, String fromUri, String toUri) {
        NodeList elements = doc.getElementsByTagName("*");
        for (int i = 0; i < elements.getLength(); i++) {
            Node element = elements.item(i);
            if (fromUri.equals(element.getNamespaceURI())) {
                doc.renameNode(element, toUri, element.getNodeName());
            }
        }
    }

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
