package guru.sfg.brewery.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * The type Rest header auth filter.
 * @INFO : 인증 관련 필터.
 */
@Slf4j
public class RestHeaderAuthFilter extends AbstractAuthenticationProcessingFilter {  //  AbstractAuthenticationProcessingFilter 해당 필터를 구현하여 인증 프로세스를 완성한다.

    public RestHeaderAuthFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
        String userName = getUsername(httpServletRequest);
        String password = getPassword(httpServletRequest);

        if (userName == null) userName = "";
        if (password == null) password = "";

        log.info("Authenticating user = {}", userName);
        log.info("Authenticating password = {}", password);

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userName, password);    //  인증에 전달.

        return !StringUtils.isEmpty(userName) ? this.getAuthenticationManager().authenticate(token) : null;

    }   //  ToDO : Step1 기본 인증 필터 구현.    여기 까지만 구현하면 인증 성공시 302를 리턴한다.

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        if (log.isDebugEnabled()) {
            log.debug("Request is to process authentication");
        }

        try {
            Authentication authResult = attemptAuthentication(request, response);
            if (authResult != null) {
                this.successfulAuthentication(request, response, chain, authResult);
            } else {
                chain.doFilter(request, response);
            }
        } catch (AuthenticationException authenticationException) {
            log.error("Authentication Failed",authenticationException);
            unsuccessfulAuthentication(request, response, authenticationException);

        }

    }   //  ToDO : Step2

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        if (log.isDebugEnabled()) {
            log.debug("Authentication success. Updating SecurityContextHolder to contain: " + authResult);
        }

        SecurityContextHolder.getContext().setAuthentication(authResult);   //  Spring Security의 컨텍스트 내에서 권한 부여를 설정한다.

    }   //  ToDO Step3


    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {

        SecurityContextHolder.clearContext();   //  SecurityContextHolder 해당 클래스가 무슨 역할을 하는지 조사해보자 ToDO

        if (log.isDebugEnabled()) {
            log.debug("Authentication request failed: " + failed.toString(), failed);
            log.debug("Updated SecurityContextHolder to contain null Authentication");
        }

        log.debug("No failure URL set, sending 401 Unauthorized error");
        response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());

    }   //  ToDO Step4


    private String getPassword(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getHeader("Api-Secret");
    }

    private String getUsername(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getHeader("Api-Key");
    }

}
