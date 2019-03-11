package no.nav.fo.veilarb.dokument;

import io.swagger.annotations.*;
import no.nav.fo.veilarb.dokument.domain.Dokumentbestilling;
import no.nav.fo.veilarb.dokument.domain.JournalpostId;
import no.nav.fo.veilarb.dokument.service.DokumentService;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import static org.apache.cxf.helpers.HttpHeaderHelper.AUTHORIZATION;

@Component
@Path("/")
@Produces("application/json")
@Api(value = "DokumentRessurs", description = "Tjeneste for bestilling av dokument og ekspedering som brev")
public class DokumentRessurs {

    private final DokumentService dokumentService;

    @Inject
    public DokumentRessurs(DokumentService dokumentService) {
        this.dokumentService = dokumentService;
    }

    @POST
    @Path("/bestilldokument")
    @ApiOperation(value = "Bestill dokument og ekspeder som brev")
    public JournalpostId bestillDokument(
            @ApiParam(value = "a oidc-token representing the consuming application", example = "Bearer ")
            @HeaderParam(AUTHORIZATION) String authorization,
            Dokumentbestilling dokumentBestilling) {
        return dokumentService.bestillDokument(dokumentBestilling);
    }
}
