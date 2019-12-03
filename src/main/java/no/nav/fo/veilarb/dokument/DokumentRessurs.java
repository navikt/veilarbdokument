package no.nav.fo.veilarb.dokument;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import no.nav.fo.veilarb.dokument.domain.DokumentbestillingDto;
import no.nav.fo.veilarb.dokument.domain.DokumentbestillingResponsDto;
import no.nav.fo.veilarb.dokument.service.DokumentService;
import no.nav.sbl.featuretoggle.unleash.UnleashService;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

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
    public DokumentRessurs(DokumentService dokumentService,
                           UnleashService unleashService) {
        this.dokumentService = dokumentService;
        this.unleashService = unleashService;
    }

    @POST
    @Path("/bestilldokument")
    @ApiOperation(value = "Bestill dokument og ekspeder som brev")
    public DokumentbestillingResponsDto bestillDokument(
            @ApiParam(value = "a oidc-token representing the consuming application", example = "Bearer ")
            @HeaderParam(AUTHORIZATION) String authorization,
            DokumentbestillingDto dokumentBestilling) {
        if (unleashService.isEnabled(VEILARBDOKUMENT_ENABLED_TOGGLE)) {
            return dokumentService.bestillDokument(dokumentBestilling);
        } else {
            throw new IllegalStateException("ikke tilgjengelig");
        }
    }

    @POST
    @Path("/dokumentutkast")
    @ApiOperation(value = "Produser dokumentutkast")
    @SneakyThrows
    @Produces("application/pdf")
    public Response produserDokumentutkast(
            @ApiParam(value = "a oidc-token representing the consuming application", example = "Bearer ")
            @HeaderParam(AUTHORIZATION) String authorization,
            DokumentbestillingDto dokumentBestilling) {
        if (unleashService.isEnabled(VEILARBDOKUMENT_ENABLED_TOGGLE)) {
            byte[] dokumentutkast = dokumentService.produserDokumentutkast(dokumentBestilling);
            return Response.ok(dokumentutkast).build();

        } else {
            throw new IllegalStateException("ikke tilgjengelig");
        }
    }
}
