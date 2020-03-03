package no.nav.fo.veilarb.dokument.config;

import net.sf.ehcache.config.CacheConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static net.sf.ehcache.store.MemoryStoreEvictionPolicy.LRU;
import static no.nav.dialogarena.aktor.AktorConfig.AKTOR_ID_FROM_FNR_CACHE;
import static no.nav.dialogarena.aktor.AktorConfig.FNR_FROM_AKTOR_ID_CACHE;
import static no.nav.sbl.dialogarena.common.abac.pep.context.AbacContext.ABAC_CACHE;

@Configuration
@EnableCaching
public class CacheConfig {

    public static final String NORG2_ENHET_KONTAKTINFO_CACHE_NAME = "NORG2_ENHET_KONTAKTINFO_CACHE";
    public static final String NORG2_ENHET_ORGANISERING_CACHE_NAME = "NORG2_ENHET_ORGANISERING_CACHE";

    private static final int ONE_HOUR_IN_SECONDS = 60 * 60;

    private static final CacheConfiguration NORG2_ENHET_KONTAKTINFO_CACHE =
            new CacheConfiguration(NORG2_ENHET_KONTAKTINFO_CACHE_NAME, 1000)
                    .memoryStoreEvictionPolicy(LRU)
                    .timeToIdleSeconds(ONE_HOUR_IN_SECONDS)
                    .timeToLiveSeconds(ONE_HOUR_IN_SECONDS);

    private static final CacheConfiguration NORG2_ENHET_ORGANISERING_CACHE =
            new CacheConfiguration(NORG2_ENHET_ORGANISERING_CACHE_NAME, 1000)
                    .memoryStoreEvictionPolicy(LRU)
                    .timeToIdleSeconds(ONE_HOUR_IN_SECONDS)
                    .timeToLiveSeconds(ONE_HOUR_IN_SECONDS);

    @Bean
    public CacheManager cacheManager() {
        net.sf.ehcache.config.Configuration config = new net.sf.ehcache.config.Configuration();
        config.addCache(ABAC_CACHE);
        config.addCache(AKTOR_ID_FROM_FNR_CACHE);
        config.addCache(FNR_FROM_AKTOR_ID_CACHE);
        config.addCache(NORG2_ENHET_KONTAKTINFO_CACHE);
        config.addCache(NORG2_ENHET_ORGANISERING_CACHE);
        return new EhCacheCacheManager(net.sf.ehcache.CacheManager.newInstance(config));
    }
}
