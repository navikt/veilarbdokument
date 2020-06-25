package no.nav.fo.veilarb.dokument.controller;

import no.nav.common.abac.Pep;
import no.nav.common.client.aktorregister.AktorregisterClient;
import no.nav.common.featuretoggle.UnleashService;
import no.nav.common.health.selftest.SelfTestCheck;
import no.nav.common.health.selftest.SelfTestUtils;
import no.nav.common.health.selftest.SelftTestCheckResult;
import no.nav.common.health.selftest.SelftestHtmlGenerator;
import no.nav.fo.veilarb.dokument.client.api.ArenaClient;
import no.nav.fo.veilarb.dokument.client.api.SakClient;
import no.nav.fo.veilarb.dokument.client.api.VeilederClient;
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
                              DokumentproduksjonV3Helsesjekk dokumentproduksjonV3Helsesjekk,
                              AktorregisterClient aktorregisterClient,
                              UnleashService unleashService,
                              Pep pep) {
        selftestChecks = Arrays.asList(
                new SelfTestCheck("DokumentproduksjonV3", true, dokumentproduksjonV3Helsesjekk),
                new SelfTestCheck("veilarbveileder", true, veilederClient),
                new SelfTestCheck("veilarbarena", true, arenaClient),
                new SelfTestCheck("sak", true, sakClient),
                new SelfTestCheck("Aktorregister", true, aktorregisterClient),
                new SelfTestCheck("ABAC", true, pep.getAbacClient()),
                new SelfTestCheck("Unleash", false, unleashService)
        );
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
