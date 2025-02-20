package com.demo.auth.authorization.core;

import com.demo.auth.authorization.core.mfa.MfaNeedAuthenticationToken;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import java.io.IOException;

@Setter
@Slf4j
public class MfaRedirectHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    private String mfaPage = "/mfa";

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws ServletException, IOException {
        if (authentication instanceof MfaNeedAuthenticationToken user && !user.isAuthenticated()) {
            log.debug("Redirect to MFA page");
            getRedirectStrategy().sendRedirect(request, response, mfaPage);
        } else {
            log.debug("MFA authentication not supported");
            super.onAuthenticationSuccess(request, response, authentication);
        }
    }
}
