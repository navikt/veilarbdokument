package no.nav.fo.veilarb.dokument.localtestapp.config;

import no.nav.common.auth.context.UserRole;
import no.nav.common.test.auth.TestAuthContextFilter;
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

}
