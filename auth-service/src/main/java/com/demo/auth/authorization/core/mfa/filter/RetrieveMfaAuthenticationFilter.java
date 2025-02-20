package com.demo.auth.authorization.core.mfa.filter;

import com.demo.auth.authorization.core.mfa.MfaNeedAuthenticationToken;
import com.demo.auth.authorization.core.mfa.token.MfaToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@Setter
public class RetrieveMfaAuthenticationFilter extends OncePerRequestFilter {
    private RequestMatcher mfaPageUrl = AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/mfa");

    private RequestMatcher mfaProcessingUrl = AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/mfa");

    private SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();

    private SecurityContextRepository securityContextRepository = new RequestAttributeSecurityContextRepository();

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication = securityContextHolderStrategy.getContext().getAuthentication();
        if (authentication instanceof MfaNeedAuthenticationToken authenticationToken) {
            if (isExpiredToken(authenticationToken) || !isMfaProcessing(request)) {
                log.debug("Retrieve MFA authentication session");
                // Remove current authentication.
                SecurityContext emptyContext = securityContextHolderStrategy.createEmptyContext();
                securityContextHolderStrategy.setContext(emptyContext);

                // Persist empty authentication.
                securityContextRepository.saveContext(emptyContext, request, response);
            }
        }
        filterChain.doFilter(request, response);
    }

    private boolean isExpiredToken(MfaNeedAuthenticationToken authenticationToken) {
        MfaToken token = authenticationToken.getToken();
        return LocalDateTime.now().isAfter(token.getExpiresAt());
    }

    private boolean isMfaProcessing(HttpServletRequest request) {
        if (mfaPageUrl.matches(request) || mfaProcessingUrl.matches(request)) {
            return true;
        }
        log.debug("Did not match request to {} or {}", mfaPageUrl, mfaProcessingUrl);
        return false;
    }

    public void setMfaPage(String mfaPageUrl) {
        this.mfaPageUrl = AntPathRequestMatcher.antMatcher(HttpMethod.GET, mfaPageUrl);
    }

    public void setMfaPage(RequestMatcher mfaPageUrl) {
        this.mfaPageUrl = mfaPageUrl;
    }

    public void setMfaProcessingUrl(String mfaProcessingUrl) {
        this.mfaProcessingUrl = AntPathRequestMatcher.antMatcher(HttpMethod.POST, mfaProcessingUrl);
    }

    public void setMfaProcessingUrl(RequestMatcher mfaProcessingUrl) {
        this.mfaProcessingUrl = mfaProcessingUrl;
    }
}
