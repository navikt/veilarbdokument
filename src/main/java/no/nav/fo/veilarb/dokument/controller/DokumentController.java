package no.nav.fo.veilarb.dokument.controller;

import lombok.extern.slf4j.Slf4j;
import no.nav.common.featuretoggle.UnleashService;
import no.nav.fo.veilarb.dokument.domain.DokumentbestillingDto;
import no.nav.fo.veilarb.dokument.domain.DokumentbestillingResponsDto;
import no.nav.fo.veilarb.dokument.service.DokumentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.core.Response;

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

    @PostMapping("/bestilldokument")
    public DokumentbestillingResponsDto bestillDokument(
            @RequestHeader(AUTHORIZATION) String authorization,
            DokumentbestillingDto dokumentBestilling) {
        if (unleashService.isEnabled(VEILARBDOKUMENT_ENABLED_TOGGLE) || unleashService.isEnabled(PTO_VEDTAKSSTOTTE_PILOT_TOGGLE)) {
            return dokumentService.bestillDokument(dokumentBestilling);
        } else {
            throw new IllegalStateException("ikke tilgjengelig");
        }
    }

    @PostMapping("/dokumentutkast")
    public Response produserDokumentutkast(
            @RequestHeader(AUTHORIZATION) String authorization,
            DokumentbestillingDto dokumentBestilling) {
        if (unleashService.isEnabled(VEILARBDOKUMENT_ENABLED_TOGGLE) || unleashService.isEnabled(PTO_VEDTAKSSTOTTE_PILOT_TOGGLE)) {
            byte[] dokumentutkast = dokumentService.produserDokumentutkast(dokumentBestilling);
            return Response.ok(dokumentutkast).build();

        } else {
            throw new IllegalStateException("ikke tilgjengelig");
        }
    }
}
