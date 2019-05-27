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
    public final String kode;

    MalType(String kode) {
        this.kode = kode;
    }

    public static String getMalKode(MalType malType) {
        switch (malType) {
            case STANDARD_INNSATS_SKAFFE_ARBEID:
                return "000132"; // TODO
            case STANDARD_INNSATS_BEHOLDE_ARBEID:
                return "000132"; // TODO
            case SITUASJONSBESTEMT_INNSATS_SKAFFE_ARBEID:
                return "000135";
            case SITUASJONSBESTEMT_INNSATS_BEHOLDE_ARBEID:
                return "000132"; // TODO
            case SPESIELT_TILPASSET_INNSATS_SKAFFE_ARBEID:
                return "000132"; // TODO
            case SPESIELT_TILPASSET_INNSATS_BEHOLDE_ARBEID:
                return "000132"; // TODO
            case GRADERT_VARIG_TILPASSET_INNSATS:
                return "000132"; // TODO
            case VARIG_TILPASSET_INNSATS:
                return "000132"; // TODO
            default:
                throw new IllegalStateException("Manglende mapping av MalType til kode");
        }
    }
}
