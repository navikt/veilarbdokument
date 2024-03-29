package no.nav.fo.veilarb.dokument.controller

import no.nav.fo.veilarb.dokument.domain.ProduserDokumentV2DTO
import no.nav.fo.veilarb.dokument.service.AuthService
import no.nav.fo.veilarb.dokument.service.DokumentV2Service
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v2")
class DokumentV2Controller(val dokumentV2Service: DokumentV2Service,
                           val authService: AuthService) {

    @PostMapping("/produserdokument")
    fun produserDokumentV2(@RequestBody dto: ProduserDokumentV2DTO): ByteArray {
            authService.sjekkTilgang(dto.brukerFnr, dto.enhetId)
            return dokumentV2Service.produserDokument(dto)
    }
}
