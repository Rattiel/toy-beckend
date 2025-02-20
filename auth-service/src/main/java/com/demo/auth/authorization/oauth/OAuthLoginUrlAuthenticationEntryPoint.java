package com.demo.auth.authorization.oauth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;

import java.io.IOException;

@Slf4j
@Setter
public class OAuthLoginUrlAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {
    private RequestCache requestCache = new HttpSessionRequestCache();

    public OAuthLoginUrlAuthenticationEntryPoint(String loginFormUrl) {
        super(loginFormUrl);
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        this.requestCache.saveRequest(request, response);
        super.commence(request, response, authException);
    }
}
