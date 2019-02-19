package no.nav.fo.veilarb.dokument;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.nav.fo.veilarb.dokument.domain.DokumentBestilling;
import no.nav.fo.veilarb.dokument.domain.JournalpostId;
import no.nav.fo.veilarb.dokument.service.DokumentService;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

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
    public JournalpostId bestillDokument(DokumentBestilling dokumentBestilling) {
        return dokumentService.bestillDokument(dokumentBestilling);
    }
}
