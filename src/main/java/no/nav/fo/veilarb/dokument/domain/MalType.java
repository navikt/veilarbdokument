package no.nav.fo.veilarb.dokument.domain;

import com.fasterxml.jackson.annotation.JsonValue;

// TODO ikke utfyllende, 1-1 med dokumentmaler
public enum MalType {

    STANDARD_INNSATS_SKAFFE_ARBEID("STANDARD_INNSATS_SKAFFE_ARBEID"),
    STANDARD_INNSATS_BEHOLDE_ARBEID("STANDARD_INNSATS_BEHOLDE_ARBEID");

    @JsonValue
    public final String name;

    MalType(String kode) {
        this.name = kode;
    }

    public static String getMalKode(MalType malType) {
        switch (malType) {
            case STANDARD_INNSATS_SKAFFE_ARBEID:
                return "000132";
            case STANDARD_INNSATS_BEHOLDE_ARBEID:
                return "000132";
            default:
                throw new IllegalStateException("Manglende mapping av MalType til kode");
        }
    }
}
