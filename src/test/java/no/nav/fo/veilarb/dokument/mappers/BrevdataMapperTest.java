package no.nav.fo.veilarb.dokument.mappers;


import no.nav.fo.veilarb.dokument.domain.Brevdata;
import no.nav.fo.veilarb.dokument.domain.MalType;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class BrevdataMapperTest {

    @Test
    public void lagBrevdata__sjekk_korrekt_mapping_av_xml_namespace_uri() {
        Stream.of(MalType.values()).forEach(malType -> {
            Brevdata brevdata = lagBrevdata(malType);

            Element element = BrevdataMapper.mapBrevdata(brevdata);

            sjekkXml(element, malType);

        });
    }

    private static String elementTilString(Element element) {
        Document document = element.getOwnerDocument();
        DOMImplementationLS domImplLS = (DOMImplementationLS) document.getImplementation();
        LSSerializer serializer = domImplLS.createLSSerializer();
        return serializer.writeToString(element);
    }

    private static Brevdata lagBrevdata(MalType malType) {
        return Brevdata.builder()
                .kilder(Arrays.asList("kilde1", "kilde2"))
                .begrunnelse("begrunnelse")
                .brukerFnr("fnr")
                .enhetId("enhetId")
                .enhetIdKontakt("enhetKontaktId")
                .veilederId("veilederId")
                .veilederNavn("veilederNavn")
                .malType(malType)
                .build();
    }

    private static void sjekkXml(Element element, MalType malType) {
        String xml = elementTilString(element);

        assertThat(xml.contains("xmlns:ns1=\"http://nav.no/dok/brevdata/felles/v2/NAVFelles\"")).isTrue();
        assertThat(xml.contains("xmlns:ns2=\"http://nav.no/dok/veilarbdokmaler/felles/veilarb_felles\"")).isTrue();
        assertThat(xml.contains("xmlns:ns3=\"http://nav.no/dok/veilarbdokmaler/" + MalType.getMalKode(malType) + "\"")).isTrue();
        assertThat(xml.contains("xmlns=")).isFalse();
        assertThat(xml.contains("xmlns:ns4=")).isFalse();
    }
}
