package no.nav.fo.veilarb.dokument.localtestapp.filter;

import no.nav.common.auth.subject.IdentType;
import no.nav.common.auth.subject.SsoToken;
import no.nav.common.auth.subject.Subject;
import no.nav.common.auth.subject.SubjectHandler;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.Collections;

import static no.nav.fo.veilarb.dokument.localtestapp.stub.Values.TEST_VEILEDER_IDENT;
import static no.nav.fo.veilarb.dokument.localtestapp.stub.Values.TEST_VEILEDER_TOKEN;

public class TestSubjectFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
        SsoToken ssoToken = SsoToken.oidcToken(TEST_VEILEDER_TOKEN, Collections.EMPTY_MAP);
        Subject testSubject = new Subject(TEST_VEILEDER_IDENT, IdentType.InternBruker, ssoToken);
        SubjectHandler.withSubject(testSubject, () -> filterChain.doFilter(servletRequest, servletResponse));
    }

}
