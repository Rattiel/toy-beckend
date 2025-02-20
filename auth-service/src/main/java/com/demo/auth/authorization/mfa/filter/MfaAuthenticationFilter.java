package com.demo.auth.authorization.mfa.filter;

import com.demo.auth.authorization.mfa.InvalidMfaRequestException;
import com.demo.auth.authorization.mfa.token.MfaAuthenticationToken;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.io.IOException;

@Setter
public class MfaAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private String codeParameter = "code";

    private static final RequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher("/mfa", "POST");

    public MfaAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER, authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String code = obtainCode(request);
        code = (code != null) ? code.trim() : "";
        Object principal = getPrincipal();
        MfaAuthenticationToken authRequest = new MfaAuthenticationToken(principal, code);
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        super.getRememberMeServices().loginFail(request, response);
        super.unsuccessfulAuthentication(request, response, failed);
    }

    @Nullable
    protected String obtainCode(HttpServletRequest request) {
        return request.getParameter(this.codeParameter);
    }

    private Object getPrincipal() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication beforeAuthentication = context.getAuthentication();
        if (beforeAuthentication == null) {
            throw new InvalidMfaRequestException("Before Authentication is null");
        }
        return beforeAuthentication.getPrincipal();
    }

    @Override
    public void setFilterProcessesUrl(String filterProcessesUrl) {
        this.setRequiresAuthenticationRequestMatcher(
                new AntPathRequestMatcher(filterProcessesUrl, "POST")
        );
    }
}
