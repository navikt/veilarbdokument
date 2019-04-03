package no.nav.fo.veilarb.dokument;

import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import no.nav.fo.veilarb.dokument.domain.Dokumentbestilling;
import no.nav.fo.veilarb.dokument.domain.DokumentbestillingRespons;
import no.nav.fo.veilarb.dokument.service.DokumentService;
import no.nav.sbl.featuretoggle.unleash.UnleashService;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.*;

import static org.apache.cxf.helpers.HttpHeaderHelper.AUTHORIZATION;

@Component
@Path("/")
@Produces("application/json")
@Api(value = "DokumentRessurs", description = "Tjeneste for bestilling av dokument og ekspedering som brev")
@Slf4j
public class DokumentRessurs {

    static final String VEILARBDOKUMENT_ENABLED_TOGGLE = "veilarbdokument.enabled";

    private final DokumentService dokumentService;
    private final UnleashService unleashService;

    @Inject
    public DokumentRessurs(DokumentService dokumentService, UnleashService unleashService) {
        this.dokumentService = dokumentService;
        this.unleashService = unleashService;
    }

    @POST
    @Path("/bestilldokument")
    @ApiOperation(value = "Bestill dokument og ekspeder som brev")
    public DokumentbestillingRespons bestillDokument(
            @ApiParam(value = "a oidc-token representing the consuming application", example = "Bearer ")
            @HeaderParam(AUTHORIZATION) String authorization,
            Dokumentbestilling dokumentBestilling) {
        if (unleashService.isEnabled(VEILARBDOKUMENT_ENABLED_TOGGLE)) {
            return dokumentService.bestillDokument(dokumentBestilling);
        } else {
            throw new IllegalStateException("ikke tilgjengelig");
        }
    }

    @POST
    @Path("/dokumentutkast")
    @ApiOperation(value = "Produser dokumentutkast")
    public byte[] produserDokumentutkast(
            @ApiParam(value = "a oidc-token representing the consuming application", example = "Bearer ")
            @HeaderParam(AUTHORIZATION) String authorization,
            Dokumentbestilling dokumentBestilling) {
        if (unleashService.isEnabled(VEILARBDOKUMENT_ENABLED_TOGGLE)) {
            return dokumentService.produserDokumentutkast(dokumentBestilling);
        } else {
            throw new IllegalStateException("ikke tilgjengelig");
        }
    }
}
