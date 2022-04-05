package no.nav.fo.veilarb.dokument.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
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

    private final DokumentService dokumentService;

    public DokumentController(DokumentService dokumentService) {
        this.dokumentService = dokumentService;
    }

    @Operation(summary = "Bestill dokument og ekspeder som brev")
    @PostMapping("/bestilldokument")
    public DokumentbestillingResponsDto bestillDokument(
            @RequestHeader(AUTHORIZATION) String authorization,
            @RequestBody DokumentbestillingDto dokumentBestilling) {
        return dokumentService.bestillDokument(dokumentBestilling);
    }

    @Operation(summary = "Produser dokumentutkast")
    @PostMapping("/dokumentutkast")
    public ResponseEntity<byte[]> produserDokumentutkast(
            @RequestHeader(AUTHORIZATION) String authorization,
            @RequestBody DokumentbestillingDto dokumentBestilling) {
        byte[] dokumentutkast = dokumentService.produserDokumentutkast(dokumentBestilling);
        return ResponseEntity.ok().body(dokumentutkast);
    }
}
