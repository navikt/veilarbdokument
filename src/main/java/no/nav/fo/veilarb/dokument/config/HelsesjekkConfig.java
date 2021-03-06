package no.nav.fo.veilarb.dokument.config;

import no.nav.common.abac.Pep;
import no.nav.common.client.aktorregister.AktorregisterClient;
import no.nav.common.featuretoggle.UnleashService;
import no.nav.common.health.selftest.SelfTestCheck;
import no.nav.common.health.selftest.SelfTestChecks;
import no.nav.common.health.selftest.SelfTestMeterBinder;
import no.nav.fo.veilarb.dokument.client.api.*;
import no.nav.fo.veilarb.dokument.helsesjekk.DokumentproduksjonV3Helsesjekk;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class HelsesjekkConfig {

    @Bean
    public SelfTestChecks selfTestChecks(VeilederClient veilederClient,
                                         PersonClient personClient,
                                         EnhetClient enhetClient,
//                                         BrevClient brevClient,
                                         ArenaClient arenaClient,
                                         SakClient sakClient,
                                         DokumentproduksjonV3Helsesjekk dokumentproduksjonV3Helsesjekk,
                                         AktorregisterClient aktorregisterClient,
                                         UnleashService unleashService,
                                         Pep pep) {
        List<SelfTestCheck> selfTestChecks = Arrays.asList(
                new SelfTestCheck("DokumentproduksjonV3", true, dokumentproduksjonV3Helsesjekk),
                new SelfTestCheck("veilarbveileder", true, veilederClient),
                new SelfTestCheck("veilarbperson", true, personClient),
                new SelfTestCheck("Norg2", true, enhetClient),
//                new SelfTestCheck("pto-pdfgen", false, brevClient), // TODO: kan tas med pto-pdfgen er prodsatt
                new SelfTestCheck("veilarbarena", true, arenaClient),
                new SelfTestCheck("sak", true, sakClient),
                new SelfTestCheck("Aktorregister", true, aktorregisterClient),
                new SelfTestCheck("ABAC", true, pep.getAbacClient()),
                new SelfTestCheck("Unleash", false, unleashService)
        );

        return new SelfTestChecks(selfTestChecks);
    }

    @Bean
    public SelfTestMeterBinder selfTestMeterBinder(SelfTestChecks selfTestChecks) {
        return new SelfTestMeterBinder(selfTestChecks);
    }
}
