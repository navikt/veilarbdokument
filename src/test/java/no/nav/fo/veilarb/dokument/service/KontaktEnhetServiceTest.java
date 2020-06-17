package no.nav.fo.veilarb.dokument.service;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import no.nav.common.rest.client.RestClient;
import no.nav.fo.veilarb.dokument.client.api.EnhetClient;
import no.nav.fo.veilarb.dokument.client.impl.EnhetClientImpl;
import no.nav.fo.veilarb.dokument.domain.Enhet;
import no.nav.fo.veilarb.dokument.domain.EnhetKontaktinformasjon;
import no.nav.fo.veilarb.dokument.domain.EnhetOrganisering;
import no.nav.fo.veilarb.dokument.domain.EnhetPostadresse;
import okhttp3.OkHttpClient;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.stream.Stream;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;

public class KontaktEnhetServiceTest {

    private EnhetClient enhetClient;
    private KontaktEnhetService kontaktEnhetService;

    private final String ENHET_NR = "1234";
    private final String EIER_ENHET_NR = "4321";
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
    public void opprinnelig_enhet_som_avsenderenhet_dersom_enhet_har_postadresse() {

        gittKontaktinformasjon(new EnhetKontaktinformasjon(ENHET_NR,
                new EnhetPostadresse("postnummer", "poststed")));

        String enhetId = kontaktEnhetService.utledKontaktEnhetId(ENHET_NR);

        assertEquals(ENHET_NR, enhetId);
    }

    @Test
    public void eierenhet_som_avsenderenhet_dersom_opprinnelig_enhet_ikke_har_postadresse() {
        gittKontaktinformasjon(new EnhetKontaktinformasjon(ENHET_NR, null));
        gittKontaktinformasjon(new EnhetKontaktinformasjon(EIER_ENHET_NR,
                new EnhetPostadresse("postnummer", "poststed")));
        gittEnhetOrganisering(ENHET_NR,
                new EnhetOrganisering("ANNEN_1", null, null, new Enhet("ANNEN_1", "navn")),
                new EnhetOrganisering("EIER", null, null, new Enhet(EIER_ENHET_NR, "navn")),
                new EnhetOrganisering("ANNEN_2", null, null, new Enhet("ANNEN_2", "navn"))
        );

        String enhetId = kontaktEnhetService.utledKontaktEnhetId(ENHET_NR);

        assertEquals(EIER_ENHET_NR, enhetId);
    }

    @Test
    public void ingen_eier() {
        gittKontaktinformasjon(new EnhetKontaktinformasjon(ENHET_NR, null));
        gittEnhetOrganisering(ENHET_NR);

        assertThatThrownBy(() ->
                kontaktEnhetService.utledKontaktEnhetId(ENHET_NR)
        ).isExactlyInstanceOf(RuntimeException.class);

    }

    @Test
    public void flere_eiere() {
        gittKontaktinformasjon(new EnhetKontaktinformasjon(ENHET_NR, null));
        gittEnhetOrganisering(ENHET_NR,
                new EnhetOrganisering("EIER", null, null, new Enhet("EIER_1", "navn")),
                new EnhetOrganisering("EIER", null, null, new Enhet("EIER_2", "navn")));

        assertThatThrownBy(() ->
                kontaktEnhetService.utledKontaktEnhetId(ENHET_NR)
        ).isExactlyInstanceOf(RuntimeException.class);
    }

    @Test
    public void eier_mangler_adresse() {
        gittKontaktinformasjon(new EnhetKontaktinformasjon(ENHET_NR, null));
        gittKontaktinformasjon(new EnhetKontaktinformasjon(EIER_ENHET_NR, null));
        gittEnhetOrganisering(ENHET_NR,
                new EnhetOrganisering("EIER", null, null, new Enhet(EIER_ENHET_NR, "navn")));

        assertThatThrownBy(() ->
                kontaktEnhetService.utledKontaktEnhetId(ENHET_NR)
        ).isExactlyInstanceOf(RuntimeException.class);
    }

    @Test
    public void eier_gyldig_fra_gyldig_til() {
        setupEierGyldighetTest(GYLDIG_FRA, GYLDIG_TIL);
        assertEquals(EIER_ENHET_NR, kontaktEnhetService.utledKontaktEnhetId(ENHET_NR));
    }

    @Test
    public void eier_ugyldig_fra_gyldig_til() {
        setupEierGyldighetTest(UGYLDIG_FRA, GYLDIG_TIL);
        assertThatThrownBy(() ->
                kontaktEnhetService.utledKontaktEnhetId(ENHET_NR)
        ).isExactlyInstanceOf(RuntimeException.class);
    }

    @Test
    public void eier_gyldig_fra_ugyldig_til() {
        setupEierGyldighetTest(GYLDIG_FRA, UGYLDIG_TIL);
        assertThatThrownBy(() ->
                kontaktEnhetService.utledKontaktEnhetId(ENHET_NR)
        ).isExactlyInstanceOf(RuntimeException.class);
    }

    @Test
    public void eier_ugyldig_fra_ugyldig_til() {
        setupEierGyldighetTest(UGYLDIG_FRA, UGYLDIG_TIL);
        assertThatThrownBy(() ->
                kontaktEnhetService.utledKontaktEnhetId(ENHET_NR)
        ).isExactlyInstanceOf(RuntimeException.class);
    }

    @Test
    public void eier_gyldig_fra_null_til() {
        setupEierGyldighetTest(GYLDIG_FRA, null);
        assertEquals(EIER_ENHET_NR, kontaktEnhetService.utledKontaktEnhetId(ENHET_NR));
    }

    @Test
    public void eier_ugyldig_fra_null_til() {
        setupEierGyldighetTest(UGYLDIG_FRA, null);
        assertThatThrownBy(() ->
                kontaktEnhetService.utledKontaktEnhetId(ENHET_NR)
        ).isExactlyInstanceOf(RuntimeException.class);
    }

    @Test
    public void eier_null_fra_gyldig_til() {
        setupEierGyldighetTest(null, GYLDIG_TIL);
        assertEquals(EIER_ENHET_NR, kontaktEnhetService.utledKontaktEnhetId(ENHET_NR));
    }

    @Test
    public void eier_null_fra_ugyldig_til() {
        setupEierGyldighetTest(null, UGYLDIG_TIL);
        assertThatThrownBy(() ->
                kontaktEnhetService.utledKontaktEnhetId(ENHET_NR)
        ).isExactlyInstanceOf(RuntimeException.class);
    }

    @Test
    public void eier_lik_fra_lik_til() {
        setupEierGyldighetTest(LocalDate.now(), LocalDate.now());
        assertEquals(EIER_ENHET_NR, kontaktEnhetService.utledKontaktEnhetId(ENHET_NR));
    }

    @Test
    public void eier_null_fra_null_til() {
        setupEierGyldighetTest(null, null);
        assertEquals(EIER_ENHET_NR, kontaktEnhetService.utledKontaktEnhetId(ENHET_NR));
    }

    private void gittKontaktinformasjon(EnhetKontaktinformasjon kontaktinformasjon) {

        String enhetNr = kontaktinformasjon.getEnhetNr();

        String postadresse = Optional.ofNullable(kontaktinformasjon.getPostadresse())
                .map(x -> String.format("{\"postnummer\": %s, \"poststed\": %s }",
                        wrapQuotes(x.getPostnummer()), wrapQuotes(x.getPoststed()))).orElse(null);

        String response = String.format("{\"enhetNr\": %s, \"postadresse\": %s }", wrapQuotes(enhetNr), postadresse);

        givenThat(
                get(urlEqualTo("/v1/enhet/" + kontaktinformasjon.getEnhetNr() + "/kontaktinformasjon"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader("Content-Type", "applicaition/json")
                                .withBody(response)
                        ));
    }

    private void gittEnhetOrganisering(String enhetNr, EnhetOrganisering... organisering) {
        String response = "[" + Stream.of(organisering).map(x -> {
            String enhet = String.format("{\"nr\": %s, \"navn\": %s}",
                    wrapQuotes(x.getOrganiserer().getNr()), wrapQuotes(x.getOrganiserer().getNavn()));
            return String.format("{\"orgType\": %s, \"organiserer\": %s, \"fra\": %s, \"til\": %s}",
                    wrapQuotes(x.getOrgType()), enhet,
                    wrapQuotes(formatDate(x.getFra())), wrapQuotes(formatDate(x.getTil())));
        }).reduce((x, y) -> x + ", " + y).orElse("") + "]";

        givenThat(
                get(urlEqualTo("/v1/enhet/" + enhetNr + "/organisering"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader("Content-Type", "applicaition/json")
                                .withBody(response)
                        ));
    }

    private String wrapQuotes(String s) {
        return Optional.ofNullable(s).map(x -> String.format("\"%s\"", x)).orElse(null);
    }

    private String formatDate(LocalDate date) {
        return Optional.ofNullable(date).map(x -> x.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))).orElse(null);
    }

    private void setupEierGyldighetTest(LocalDate gyldigFra, LocalDate gyldigTil) {
        gittKontaktinformasjon(new EnhetKontaktinformasjon(ENHET_NR, null));
        gittKontaktinformasjon(new EnhetKontaktinformasjon(EIER_ENHET_NR,
                new EnhetPostadresse("postnummer", "poststed")));
        gittEnhetOrganisering(ENHET_NR,
                new EnhetOrganisering("EIER", gyldigFra, gyldigTil,
                        new Enhet(EIER_ENHET_NR, "navn")));
    }
}
