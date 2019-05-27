package no.nav.fo.veilarb.dokument.mappers;


import no.nav.fo.veilarb.dokument.domain.Brevdata;
import no.nav.fo.veilarb.dokument.domain.MalType;
import no.nav.fo.veilarb.dokument.domain.Person;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;

import java.util.Arrays;

public class BrevdataMapperTest {

    @Test
    public void foo() {
        Brevdata brevdata = Brevdata.builder()
                .veilederNavn("Test")
                .kilder(Arrays.asList("argegr esbt", "argsdth setbs nhn"))
                .begrunnelse("wrgwrg")
                .bruker(new Person("123123"))
                .mottaker(new Person("123123"))
                .veilederEnhet("enhet")
                .veilederId("2345")
                .veilederNavn("veilederen")
                .malType(MalType.GRADERT_VARIG_TILPASSET_INNSATS)
                .build();
        Element element = BrevdataMapper.mapBrevdata(brevdata);

        Document document = element.getOwnerDocument();
        DOMImplementationLS domImplLS = (DOMImplementationLS) document.getImplementation();
        LSSerializer serializer = domImplLS.createLSSerializer();
        String str = serializer.writeToString(element);

        System.out.println(str);


    }
}