package no.nav.fo.veilarb.dokument.controller

import no.nav.fo.veilarb.dokument.domain.DokumentbestillingDto
import no.nav.fo.veilarb.dokument.service.DokumentV2Service
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v2")
class DokumentV2Controller(val dokumentV2Service: DokumentV2Service) {

    @PostMapping("/dokumentutkast")
    fun dokumentutkast(@RequestBody dokumentBestilling: DokumentbestillingDto): ByteArray {
            return dokumentV2Service.lagDokumentutkast(dokumentBestilling)
    }
}
