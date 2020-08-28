package no.nav.fo.veilarb.dokument.config;

import no.nav.common.abac.Pep;
import no.nav.common.client.aktorregister.AktorregisterClient;
import no.nav.common.featuretoggle.UnleashService;
import no.nav.common.health.selftest.SelfTestCheck;
import no.nav.common.health.selftest.SelfTestChecks;
import no.nav.fo.veilarb.dokument.client.api.ArenaClient;
import no.nav.fo.veilarb.dokument.client.api.SakClient;
import no.nav.fo.veilarb.dokument.client.api.VeilederClient;
import no.nav.fo.veilarb.dokument.helsesjekk.DokumentproduksjonV3Helsesjekk;
import no.nav.fo.veilarb.dokument.util.SelfTestMeterBinderTemp;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class HelsesjekkConfig {

    @Bean
    public SelfTestChecks selfTestChecks(VeilederClient veilederClient,
                                         ArenaClient arenaClient,
                                         SakClient sakClient,
                                         DokumentproduksjonV3Helsesjekk dokumentproduksjonV3Helsesjekk,
                                         AktorregisterClient aktorregisterClient,
                                         UnleashService unleashService,
                                         Pep pep) {
        List<SelfTestCheck> selfTestChecks = Arrays.asList(
                new SelfTestCheck("DokumentproduksjonV3", true, dokumentproduksjonV3Helsesjekk),
                new SelfTestCheck("veilarbveileder", true, veilederClient),
                new SelfTestCheck("veilarbarena", true, arenaClient),
                new SelfTestCheck("sak", true, sakClient),
                new SelfTestCheck("Aktorregister", true, aktorregisterClient),
                new SelfTestCheck("ABAC", true, pep.getAbacClient()),
                new SelfTestCheck("Unleash", false, unleashService)
        );

        return new SelfTestChecks(selfTestChecks);
    }

    @Bean
    public SelfTestMeterBinderTemp selfTestMeterBinder(SelfTestChecks selfTestChecks) {
        return new SelfTestMeterBinderTemp(selfTestChecks);
    }
}
