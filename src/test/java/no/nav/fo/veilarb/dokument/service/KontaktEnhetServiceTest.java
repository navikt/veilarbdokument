package no.nav.fo.veilarb.dokument.service;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import no.nav.common.rest.client.RestClient;
import no.nav.common.types.identer.EnhetId;
import no.nav.fo.veilarb.dokument.client.api.EnhetClient;
import no.nav.fo.veilarb.dokument.client.impl.EnhetClientImpl;
import no.nav.fo.veilarb.dokument.domain.EnhetKontaktinformasjon;
import no.nav.fo.veilarb.dokument.domain.EnhetPostboksadresse;
import no.nav.fo.veilarb.dokument.domain.EnhetStedsadresse;
import okhttp3.OkHttpClient;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static no.nav.fo.veilarb.dokument.utils.TestUtils.readTestResourceFile;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class KontaktEnhetServiceTest {

    private EnhetClient enhetClient;
    private KontaktEnhetService kontaktEnhetService;

    private final EnhetId ENHET_NR = EnhetId.of("1234");
    private final EnhetId EIER_ENHET_NR = EnhetId.of("4321");
    private final LocalDate GYLDIG_FRA = LocalDate.now().minusDays(2);
    private final LocalDate UGYLDIG_FRA = LocalDate.now().plusDays(1);
    private final LocalDate GYLDIG_TIL = LocalDate.now().plusDays(2);
    private final LocalDate UGYLDIG_TIL = LocalDate.now().minusDays(1);

    @Rule
    public WireMockRule wireMockRule = new WireMockRule();

    @Before
    public void setup() {
        OkHttpClient client = RestClient.baseClient();
        enhetClient = new EnhetClientImpl(client, "http://localhost:" + wireMockRule.port());
        kontaktEnhetService = new KontaktEnhetService(enhetClient);
    }

    @Test
    public void opprinnelig_enhet_som_avsenderenhet_dersom_enhet_har_postboksadresse() {

        gittEnhetMedKontaktinfoPostboksadresse(ENHET_NR);

        EnhetKontaktinformasjon enhetKontaktinformasjon = kontaktEnhetService.utledEnhetKontaktinformasjon(ENHET_NR);

        assertEquals(ENHET_NR, enhetKontaktinformasjon.getEnhetNr());
        assertTrue(enhetKontaktinformasjon.getPostadresse() instanceof EnhetPostboksadresse);
        EnhetPostboksadresse adresse = (EnhetPostboksadresse) enhetKontaktinformasjon.getPostadresse();
        assertEquals(adresse.getPostnummer(), "1234");
        assertEquals(adresse.getPoststed(), "STED");
        assertEquals(adresse.getPostboksnummer(), "1");
        assertEquals(adresse.getPostboksanlegg(), "Anlegg");
    }

    @Test
    public void opprinnelig_enhet_som_avsenderenhet_dersom_enhet_har_stedssadresse() {

        gittEnhetMedKontaktinfoStedsadresse(ENHET_NR);

        EnhetKontaktinformasjon enhetKontaktinformasjon = kontaktEnhetService.utledEnhetKontaktinformasjon(ENHET_NR);

        assertEquals(ENHET_NR, enhetKontaktinformasjon.getEnhetNr());
        assertTrue(enhetKontaktinformasjon.getPostadresse() instanceof EnhetStedsadresse);
        EnhetStedsadresse adresse = (EnhetStedsadresse) enhetKontaktinformasjon.getPostadresse();
        assertEquals(adresse.getPostnummer(), "1234");
        assertEquals(adresse.getPoststed(), "STED");
        assertEquals(adresse.getGatenavn(), "GATE");
        assertEquals(adresse.getHusnummer(), "1");
        assertEquals(adresse.getHusbokstav(), "A");
        assertEquals(adresse.getAdresseTilleggsnavn(), "Tilleggsnavn");
    }

    @Test
    public void eierenhet_som_avsenderenhet_dersom_opprinnelig_enhet_ikke_har_postadresse() {
        gittEnhetUtenKontaktinfo(ENHET_NR);
        gittEnhetMedKontaktinfoPostboksadresse(EIER_ENHET_NR);

        HashMap<String, String> replace = new HashMap<>();
        replace.put("ORG_TYPE_1", "EIER");
        replace.put("ENHET_NR_1", EIER_ENHET_NR.get());

        gittEnhetOrganiseringMed3Enheter(ENHET_NR, replace);

        EnhetKontaktinformasjon enhetKontaktinformasjon = kontaktEnhetService.utledEnhetKontaktinformasjon(ENHET_NR);

        assertEquals(EIER_ENHET_NR, enhetKontaktinformasjon.getEnhetNr());
    }

    @Test
    public void ingen_eier() {
        gittEnhetUtenKontaktinfo(ENHET_NR);
        gittEnhetOrganiseringMed3Enheter(ENHET_NR, new HashMap<>());

        assertThatThrownBy(() ->
                kontaktEnhetService.utledEnhetKontaktinformasjon(ENHET_NR)
        ).isExactlyInstanceOf(RuntimeException.class);

    }

    @Test
    public void flere_eiere() {
        gittEnhetUtenKontaktinfo(ENHET_NR);
        HashMap<String, String> replace = new HashMap<>();
        replace.put("ORG_TYPE_1", "EIER");
        replace.put("ORG_TYPE_2", "EIER");

        gittEnhetOrganiseringMed3Enheter(ENHET_NR, replace);

        assertThatThrownBy(() ->
                kontaktEnhetService.utledEnhetKontaktinformasjon(ENHET_NR)
        ).isExactlyInstanceOf(RuntimeException.class);
    }

    @Test
    public void eier_mangler_adresse() {
        gittEnhetUtenKontaktinfo(ENHET_NR);
        gittEnhetUtenKontaktinfo(EIER_ENHET_NR);

        HashMap<String, String> replace = new HashMap<>();
        replace.put("ORG_TYPE_1", "EIER");
        replace.put("ENHET_NR_1", EIER_ENHET_NR.get());

        gittEnhetOrganiseringMed3Enheter(ENHET_NR, replace);

        assertThatThrownBy(() ->
                kontaktEnhetService.utledEnhetKontaktinformasjon(ENHET_NR)
        ).isExactlyInstanceOf(RuntimeException.class);
    }

    @Test
    public void eier_gyldig_fra_gyldig_til() {
        setupEierGyldighetTest(GYLDIG_FRA, GYLDIG_TIL);
        assertEquals(EIER_ENHET_NR, kontaktEnhetService.utledEnhetKontaktinformasjon(ENHET_NR).getEnhetNr());
    }

    @Test
    public void eier_ugyldig_fra_gyldig_til() {
        setupEierGyldighetTest(UGYLDIG_FRA, GYLDIG_TIL);
        assertThatThrownBy(() ->
                kontaktEnhetService.utledEnhetKontaktinformasjon(ENHET_NR)
        ).isExactlyInstanceOf(RuntimeException.class);
    }

    @Test
    public void eier_gyldig_fra_ugyldig_til() {
        setupEierGyldighetTest(GYLDIG_FRA, UGYLDIG_TIL);
        assertThatThrownBy(() ->
                kontaktEnhetService.utledEnhetKontaktinformasjon(ENHET_NR)
        ).isExactlyInstanceOf(RuntimeException.class);
    }

    @Test
    public void eier_ugyldig_fra_ugyldig_til() {
        setupEierGyldighetTest(UGYLDIG_FRA, UGYLDIG_TIL);
        assertThatThrownBy(() ->
                kontaktEnhetService.utledEnhetKontaktinformasjon(ENHET_NR)
        ).isExactlyInstanceOf(RuntimeException.class);
    }

    @Test
    public void eier_gyldig_fra_null_til() {
        setupEierGyldighetTest(GYLDIG_FRA, null);
        assertEquals(EIER_ENHET_NR, kontaktEnhetService.utledEnhetKontaktinformasjon(ENHET_NR).getEnhetNr());
    }

    @Test
    public void eier_ugyldig_fra_null_til() {
        setupEierGyldighetTest(UGYLDIG_FRA, null);
        assertThatThrownBy(() ->
                kontaktEnhetService.utledEnhetKontaktinformasjon(ENHET_NR)
        ).isExactlyInstanceOf(RuntimeException.class);
    }

    @Test
    public void eier_null_fra_gyldig_til() {
        setupEierGyldighetTest(null, GYLDIG_TIL);
        assertEquals(EIER_ENHET_NR, kontaktEnhetService.utledEnhetKontaktinformasjon(ENHET_NR).getEnhetNr());
    }

    @Test
    public void eier_null_fra_ugyldig_til() {
        setupEierGyldighetTest(null, UGYLDIG_TIL);
        assertThatThrownBy(() ->
                kontaktEnhetService.utledEnhetKontaktinformasjon(ENHET_NR)
        ).isExactlyInstanceOf(RuntimeException.class);
    }

    @Test
    public void eier_lik_fra_lik_til() {
        setupEierGyldighetTest(LocalDate.now(), LocalDate.now());
        assertEquals(EIER_ENHET_NR, kontaktEnhetService.utledEnhetKontaktinformasjon(ENHET_NR).getEnhetNr());
    }

    @Test
    public void eier_null_fra_null_til() {
        setupEierGyldighetTest(null, null);
        assertEquals(EIER_ENHET_NR, kontaktEnhetService.utledEnhetKontaktinformasjon(ENHET_NR).getEnhetNr());
    }

    private void gittEnhetUtenKontaktinfo(EnhetId enhetId) {
        String json = readTestResourceFile("norg2/kontaktinformasjon_uten_adresse.json");
        String response = json.replace("ENHET_NR", enhetId.get());

        gittKontaktinformasjonResponse(enhetId, response);
    }
    private void gittEnhetMedKontaktinfoStedsadresse(EnhetId enhetId) {
        String json = readTestResourceFile("norg2/kontaktinformasjon_med_stedsadresse.json");
        String response = json.replace("ENHET_NR", enhetId.get());

        gittKontaktinformasjonResponse(enhetId, response);
    }
    private void gittEnhetMedKontaktinfoPostboksadresse(EnhetId enhetId) {
        String json = readTestResourceFile("norg2/kontaktinformasjon_med_postboksadresse.json");
        String response = json.replace("ENHET_NR", enhetId.get());

        gittKontaktinformasjonResponse(enhetId, response);

    }

    private void gittKontaktinformasjonResponse(EnhetId enhetId, String response) {
        givenThat(
                get(urlEqualTo("/v1/enhet/" + enhetId.get() + "/kontaktinformasjon"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader("Content-Type", "application/json")
                                .withBody(response)
                        ));
    }

    private void gittEnhetOrganiseringMed3Enheter(EnhetId enhetNr, Map<String, String> replace) {

        String json = readTestResourceFile("norg2/organisering.json");

        replace.putIfAbsent("FRA_1", "null");
        replace.putIfAbsent("TIL_1", "null");
        replace.putIfAbsent("FRA_2", "null");
        replace.putIfAbsent("TIL_2", "null");
        replace.putIfAbsent("FRA_3", "null");
        replace.putIfAbsent("TIL_3", "null");

        String response = replace.entrySet().stream().reduce(json, (a,b) -> a.replace(b.getKey(), b.getValue()), (a,b) -> b);

        givenThat(
                get(urlEqualTo("/v1/enhet/" + enhetNr.get() + "/organisering"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader("Content-Type", "application/json")
                                .withBody(response)
                        ));
    }

    private void setupEierGyldighetTest(LocalDate gyldigFra, LocalDate gyldigTil) {
        gittEnhetUtenKontaktinfo(ENHET_NR);
        gittEnhetMedKontaktinfoPostboksadresse(EIER_ENHET_NR);

        HashMap<String, String> replace = new HashMap<>();
        replace.put("ORG_TYPE_2", "EIER");
        replace.put("ENHET_NR_2", EIER_ENHET_NR.get());
        replace.put("FRA_2", wrapQuotes(formatDate(gyldigFra)));
        replace.put("TIL_2", wrapQuotes(formatDate(gyldigTil)));

        gittEnhetOrganiseringMed3Enheter(ENHET_NR, replace);
    }

    private String wrapQuotes(String s) {
        return Optional.ofNullable(s).map(x -> String.format("\"%s\"", x)).orElse(null);
    }

    private String formatDate(LocalDate date) {
        return Optional.ofNullable(date).map(x -> x.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))).orElse(null);
    }

}
