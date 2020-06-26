package no.nav.fo.veilarb.dokument.localtestapp.config;

import no.nav.fo.veilarb.dokument.localtestapp.filter.TestSubjectFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterTestConfig {

    @Bean
    public FilterRegistrationBean testSubjectFilterRegistrationBean() {
        FilterRegistrationBean<TestSubjectFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new TestSubjectFilter());
        registration.setOrder(1);
        registration.addUrlPatterns("/api/*");
        return registration;
    }

}