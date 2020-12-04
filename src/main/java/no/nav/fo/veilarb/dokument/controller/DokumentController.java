package no.nav.fo.veilarb.dokument.controller;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import no.nav.common.featuretoggle.UnleashService;
import no.nav.common.utils.EnvironmentUtils;
import no.nav.fo.veilarb.dokument.domain.DokumentbestillingDto;
import no.nav.fo.veilarb.dokument.domain.DokumentbestillingResponsDto;
import no.nav.fo.veilarb.dokument.service.DokumentService;
import no.nav.fo.veilarb.dokument.service.DokumentV2Service;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static no.nav.fo.veilarb.dokument.util.Toggles.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("/api")
@Slf4j
public class DokumentController {

    private final DokumentService dokumentService;
    private final DokumentV2Service dokumentV2Service;
    private final UnleashService unleashService;

    public DokumentController(DokumentService dokumentService,
                              UnleashService unleashService,
                              DokumentV2Service dokumentV2Service) {
        this.dokumentService = dokumentService;
        this.unleashService = unleashService;
        this.dokumentV2Service = dokumentV2Service;
    }

    @ApiOperation("Bestill dokument og ekspeder som brev")
    @PostMapping("/bestilldokument")
    public DokumentbestillingResponsDto bestillDokument(
            @RequestHeader(AUTHORIZATION) String authorization,
            @RequestBody DokumentbestillingDto dokumentBestilling) {
        return dokumentService.bestillDokument(dokumentBestilling);
    }

    private final boolean isDevelopment = EnvironmentUtils.isDevelopment().orElse(false);

    @ApiOperation("Produser dokumentutkast")
    @PostMapping("/dokumentutkast")
    public ResponseEntity<byte[]> produserDokumentutkast(
            @RequestHeader(AUTHORIZATION) String authorization,
            @RequestBody DokumentbestillingDto dokumentBestilling) {

        if (isDevelopment && unleashService.isEnabled(VEILARBDOKUMENT_V2_API_ENABLED_TOGGLE)) {
            byte[] dokumentutkastV2 = dokumentV2Service.produserDokumentutkast(dokumentBestilling);
            return ResponseEntity.ok().body(dokumentutkastV2);
        }

        byte[] dokumentutkast = dokumentService.produserDokumentutkast(dokumentBestilling);
        return ResponseEntity.ok().body(dokumentutkast);
    }
}
