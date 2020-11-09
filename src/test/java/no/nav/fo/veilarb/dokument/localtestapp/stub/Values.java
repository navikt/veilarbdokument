package no.nav.fo.veilarb.dokument.localtestapp.stub;

import no.nav.common.types.identer.AktorId;
import no.nav.common.types.identer.EnhetId;
import no.nav.common.types.identer.NavIdent;
import no.nav.fo.veilarb.dokument.domain.Adresse;
import no.nav.fo.veilarb.dokument.domain.EnhetPostboksadresse;
import no.nav.fo.veilarb.dokument.domain.Målform;

public class Values {
    public static final NavIdent TEST_VEILEDER_IDENT = NavIdent.of("X123456");
    public static final String TEST_SRV_USERNAME = "username";
    public static final String TEST_SRV_PASSWORD = "password";
    public static final AktorId TEST_AKTOR_ID = AktorId.of("678");
    public static final String TEST_JOURNALPOST_ID = "234";
    public static final String TEST_DOKUMENT_ID = "345";
    public static final EnhetId TEST_ENHET = EnhetId.of("456");
    public static final String TEST_ENHET_NAVN = "Enhet Navn";
    public static final EnhetPostboksadresse TEST_ENHET_ADRESSE =
            new EnhetPostboksadresse(
                    "postnummer",
                    "poststed",
                    "postboksnummer",
                    "postboksanlegg");
    public static final String TEST_ARENA_OPPFOLGINGSSAK = "890";
    public static final boolean TEST_UNLEASH_ENABLED = true;
    public static final int TEST_SAK_ID = 789;
    public static final String TEST_DOKUMENTUTKAST_STR = "dokumentutkast";
    public static final String TEST_PERSON_NAVN = "Navn Navnesen";
    public static final Målform TEST_MÅLFORM = Målform.NB;
    public static final Adresse TEST_ADRESSE = new Adresse(
            "adresselinje1",
            "adresselinje2",
            "adresselinje3",
            "postnummer",
            "poststed"

    );

}
