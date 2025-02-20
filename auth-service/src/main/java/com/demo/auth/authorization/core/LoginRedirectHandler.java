package com.demo.auth.authorization.core;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;

import java.io.IOException;

@Setter
@Slf4j
public class LoginRedirectHandler implements AccessDeniedHandler {
    private String loginFormUrl = "/login";

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    private RequestCache requestCache = new HttpSessionRequestCache();

    private SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();

    private SecurityContextRepository securityContextRepository = new RequestAttributeSecurityContextRepository();

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException
    ) throws IOException {
        // Remove current authentication.
        SecurityContext emptyContext = securityContextHolderStrategy.createEmptyContext();
        securityContextHolderStrategy.setContext(emptyContext);

        // Persist empty authentication.
        securityContextRepository.saveContext(emptyContext, request, response);

        // Redirect to login page.
        requestCache.saveRequest(request, response);
        redirectStrategy.sendRedirect(request, response, loginFormUrl);
    }
}
