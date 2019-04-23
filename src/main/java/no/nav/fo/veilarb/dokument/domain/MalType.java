package no.nav.fo.veilarb.dokument.domain;

import com.fasterxml.jackson.annotation.JsonValue;

public enum MalType {

    STANDARD_INNSATS_SKAFFE_ARBEID("STANDARD_INNSATS_SKAFFE_ARBEID"),
    STANDARD_INNSATS_BEHOLDE_ARBEID("STANDARD_INNSATS_BEHOLDE_ARBEID"),
    SITUASJONSBESTEMT_INNSATS_SKAFFE_ARBEID("SITUASJONSBESTEMT_INNSATS_SKAFFE_ARBEID"),
    SITUASJONSBESTEMT_INNSATS_BEHOLDE_ARBEID("SITUASJONSBESTEMT_INNSATS_BEHOLDE_ARBEID"),
    SPESIELT_TILPASSET_INNSATS_SKAFFE_ARBEID("SPESIELT_TILPASSET_INNSATS_SKAFFE_ARBEID"),
    SPESIELT_TILPASSET_INNSATS_BEHOLDE_ARBEID("SPESIELT_TILPASSET_INNSATS_BEHOLDE_ARBEID"),
    GRADERT_VARIG_TILPASSET_INNSATS("GRADERT_VARIG_TILPASSET_INNSATS"),
    VARIG_TILPASSET_INNSATS("VARIG_TILPASSET_INNSATS");

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
            case SITUASJONSBESTEMT_INNSATS_SKAFFE_ARBEID:
                return "000132";
            case SITUASJONSBESTEMT_INNSATS_BEHOLDE_ARBEID:
                return "000132";
            case SPESIELT_TILPASSET_INNSATS_SKAFFE_ARBEID:
                return "000132";
            case SPESIELT_TILPASSET_INNSATS_BEHOLDE_ARBEID:
                return "000132";
            case GRADERT_VARIG_TILPASSET_INNSATS:
                return "000132";
            case VARIG_TILPASSET_INNSATS:
                return "000132";
            default:
                throw new IllegalStateException("Manglende mapping av MalType til kode");
        }
    }
}
