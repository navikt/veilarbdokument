package no.nav.fo.veilarb.dokument.controller;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import no.nav.common.featuretoggle.UnleashService;
import no.nav.fo.veilarb.dokument.domain.DokumentbestillingDto;
import no.nav.fo.veilarb.dokument.domain.DokumentbestillingResponsDto;
import no.nav.fo.veilarb.dokument.service.DokumentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("/api")
@Slf4j
public class DokumentController {

    static final String VEILARBDOKUMENT_ENABLED_TOGGLE = "veilarbdokument.enabled";
    static final String PTO_VEDTAKSSTOTTE_PILOT_TOGGLE = "pto.vedtaksstotte.pilot";

    private final DokumentService dokumentService;
    private final UnleashService unleashService;

    public DokumentController(DokumentService dokumentService,
                              UnleashService unleashService) {
        this.dokumentService = dokumentService;
        this.unleashService = unleashService;
    }

    @ApiOperation("Bestill dokument og ekspeder som brev")
    @PostMapping("/bestilldokument")
    public DokumentbestillingResponsDto bestillDokument(
            @RequestHeader(AUTHORIZATION) String authorization,
            @RequestBody DokumentbestillingDto dokumentBestilling) {
        if (unleashService.isEnabled(VEILARBDOKUMENT_ENABLED_TOGGLE) || unleashService.isEnabled(PTO_VEDTAKSSTOTTE_PILOT_TOGGLE)) {
            return dokumentService.bestillDokument(dokumentBestilling);
        } else {
            throw new IllegalStateException("ikke tilgjengelig");
        }
    }

    @ApiOperation("Produser dokumentutkast")
    @PostMapping("/dokumentutkast")
    public ResponseEntity<byte[]> produserDokumentutkast(
            @RequestHeader(AUTHORIZATION) String authorization,
            @RequestBody DokumentbestillingDto dokumentBestilling) {
        if (unleashService.isEnabled(VEILARBDOKUMENT_ENABLED_TOGGLE) || unleashService.isEnabled(PTO_VEDTAKSSTOTTE_PILOT_TOGGLE)) {
            byte[] dokumentutkast = dokumentService.produserDokumentutkast(dokumentBestilling);
            return ResponseEntity.ok().body(dokumentutkast);

        } else {
            throw new IllegalStateException("ikke tilgjengelig");
        }
    }
}
