package no.nav.fo.veilarb.dokument.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cache.caffeine.CaffeineCache;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    public static final String NORG2_ENHET_KONTAKTINFO_CACHE_NAME = "NORG2_ENHET_KONTAKTINFO_CACHE";
    public static final String NORG2_ENHET_ORGANISERING_CACHE_NAME = "NORG2_ENHET_ORGANISERING_CACHE";


    @Bean
    public Cache norg2EnhetKontaktinfoCache() {
        return new CaffeineCache(NORG2_ENHET_KONTAKTINFO_CACHE_NAME, Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.HOURS)
                .maximumSize(1000)
        .build());
    }

    @Bean
    public Cache norg2EnhetOrganiseringCache() {
        return new CaffeineCache(NORG2_ENHET_ORGANISERING_CACHE_NAME, Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.HOURS)
                .maximumSize(1000)
                .build());
    }
}
