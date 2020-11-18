package no.nav.fo.veilarb.dokument.localtestapp.config;

import no.nav.common.auth.context.UserRole;
import no.nav.common.featuretoggle.UnleashService;
import no.nav.common.test.auth.TestAuthContextFilter;
import no.nav.fo.veilarb.dokument.filter.ToggleFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static no.nav.fo.veilarb.dokument.localtestapp.stub.Values.TEST_VEILEDER_IDENT;

@Configuration
public class FilterTestConfig {

    @Bean
    public FilterRegistrationBean testSubjectFilterRegistrationBean() {
        FilterRegistrationBean<TestAuthContextFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new TestAuthContextFilter(UserRole.INTERN, TEST_VEILEDER_IDENT.get()));
        registration.setOrder(1);
        registration.addUrlPatterns("/api/*");
        return registration;
    }

    @Bean
    public FilterRegistrationBean toggleFilterRegistrationBean(UnleashService unleashService) {
        FilterRegistrationBean<ToggleFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new ToggleFilter(unleashService));
        registration.setOrder(2);
        registration.addUrlPatterns("/api/*");
        return registration;
    }

}
