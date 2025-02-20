package com.demo.auth.authorization.mfa;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import java.io.IOException;

@Slf4j
@Setter
public class MfaAuthenticationRetryHandler extends SimpleUrlAuthenticationFailureHandler {
    private String loginPage = "/login";

    public MfaAuthenticationRetryHandler() {
    }

    public MfaAuthenticationRetryHandler(String defaultFailureUrl) {
        super(defaultFailureUrl);
    }

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception
    ) throws IOException, ServletException {
        if (exception instanceof InvalidMfaRequestException) {
            log.debug("Invalid MFA request: [{}]", exception.getMessage());
            this.getRedirectStrategy().sendRedirect(request, response, loginPage);
        }
        super.onAuthenticationFailure(request, response, exception);
    }
}
