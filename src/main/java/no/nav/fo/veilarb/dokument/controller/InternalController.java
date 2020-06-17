package no.nav.fo.veilarb.dokument.controller;

import no.nav.common.health.selftest.SelfTestCheck;
import no.nav.common.health.selftest.SelfTestUtils;
import no.nav.common.health.selftest.SelftTestCheckResult;
import no.nav.common.health.selftest.SelftestHtmlGenerator;
import no.nav.fo.veilarb.dokument.client.ArenaClient;
import no.nav.fo.veilarb.dokument.client.SakClient;
import no.nav.fo.veilarb.dokument.client.VeilederClient;
import no.nav.fo.veilarb.dokument.helsesjekk.DokumentproduksjonV3Helsesjekk;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

import static no.nav.common.health.selftest.SelfTestUtils.checkAllParallel;

@RestController
@RequestMapping("/internal")
public class InternalController {

    private final List<SelfTestCheck> selftestChecks;

    public InternalController(VeilederClient veilederClient,
                              ArenaClient arenaClient,
                              SakClient sakClient,
                              DokumentproduksjonV3Helsesjekk dokumentproduksjonV3Helsesjekk) {
        selftestChecks = Arrays.asList(
                new SelfTestCheck("veilarbveileder", false, veilederClient),
                new SelfTestCheck("veilarbarena", false, arenaClient),
                new SelfTestCheck("sak", false, sakClient),
                new SelfTestCheck("DokumentproduksjonV3", false, dokumentproduksjonV3Helsesjekk)
        );
    }

    @GetMapping("/isReady")
    public void isReady() {
    }

    @GetMapping("/isAlive")
    public void isAlive() {
    }

    @GetMapping("/selftest")
    public ResponseEntity<String> selftest() {
        List<SelftTestCheckResult> checkResults = checkAllParallel(selftestChecks);
        String html = SelftestHtmlGenerator.generate(checkResults);
        int status = SelfTestUtils.findHttpStatusCode(checkResults, true);

        return ResponseEntity
                .status(status)
                .contentType(MediaType.TEXT_HTML)
                .body(html);
    }
}
