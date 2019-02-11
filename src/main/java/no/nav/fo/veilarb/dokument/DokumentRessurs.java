package no.nav.fo.veilarb.dokument;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Component;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Component
@Path("/")
@Produces("application/json")
@Api(value = "DokumentRessurs", description = "Tjeneste for bestilling av dokument og ekspedering som brev")
public class DokumentRessurs {

    @POST
    @Path("/bestilldokument")
    @ApiOperation(value = "Bestill dokument og ekspeder som brev")
    public JournalpostID bestillDokument(DokumentBestilling dokumentBestilling) {
        return JournalpostID.of(null); // TODO
    }
}
