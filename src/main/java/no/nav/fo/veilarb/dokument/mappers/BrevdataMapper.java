package no.nav.fo.veilarb.dokument.mappers;

import lombok.SneakyThrows;
import lombok.val;
import no.nav.dok.brevdata.felles.v1.navfelles.*;
import no.nav.dok.brevdata.felles.v1.simpletypes.AktoerType;
import no.nav.dok.veilarbdokmaler._000132.BrevdataType;
import no.nav.fo.veilarb.dokument.domain.Brevdata;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import no.nav.dok.veilarbdokmaler.felles.arena_felles.VeilArbNAVFelles;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilderFactory;

public class BrevdataMapper {
    @SneakyThrows
    public static Element mapBrevdata(Brevdata brevdata) {

        Element brevdataElement = null;
        switch (brevdata.malType()) {
            case STANDARD_INNSATS_SKAFFE_ARBEID:
                brevdataElement = map000132BrevdataType(brevdata);
                break;
            case STANDARD_INNSATS_BEHOLDE_ARBEID:
                brevdataElement = map000132BrevdataType(brevdata);
                break;
        }

        return brevdataElement;
    }

    @SneakyThrows
    private static <T> Marshaller createMarshaller(Class<T> clazz) {
        JAXBContext context = JAXBContext.newInstance(clazz);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        return marshaller;
    }

    @SneakyThrows
    private static <T> Element marshalBrevdata(JAXBElement<T> brevdataElement) {
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        createMarshaller(brevdataElement.getDeclaredType()).marshal(brevdataElement, document);
        return document.getDocumentElement();
    }

    private static Element map000132BrevdataType(Brevdata brevdata) {
        val objectFactory = new no.nav.dok.veilarbdokmaler._000132.ObjectFactory();
        val brevdataType = objectFactory.createBrevdataType();
        val fag = objectFactory.createFagType();
        fag.setFritekstBegrunnelse(brevdata.begrunnelse());
        brevdataType.setFag(fag);
        brevdataType.setNAVFelles(mapFelles(brevdata));
        JAXBElement<BrevdataType> brevdataElement = objectFactory.createBrevdata(
                brevdataType
        );

        return marshalBrevdata(brevdataElement);
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
                                .withAnsattId(brevdata.veilederId())
                                .build())
                .withNavEnhet(
                        NavEnhet.builder()
                                .withEnhetsId(brevdata.veilederEnhet())
                                .build())
                .build();
    }
}
