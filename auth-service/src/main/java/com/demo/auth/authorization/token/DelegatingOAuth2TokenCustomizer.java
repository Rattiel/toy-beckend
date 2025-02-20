
package com.demo.auth.authorization.token;

import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DelegatingOAuth2TokenCustomizer implements OAuth2TokenCustomizer<JwtEncodingContext> {
    private final List<OAuth2TokenCustomizer<JwtEncodingContext>> tokenCustomizers;

    @SafeVarargs
    public DelegatingOAuth2TokenCustomizer(OAuth2TokenCustomizer<JwtEncodingContext>... tokenCustomizers) {
        Assert.notEmpty(tokenCustomizers, "tokenCustomizers cannot be empty");
        Assert.noNullElements(tokenCustomizers, "tokenCustomizers cannot be null");
        this.tokenCustomizers = Collections.unmodifiableList(Arrays.asList(tokenCustomizers));
    }

    @Override
    public void customize(JwtEncodingContext context) {
        for (OAuth2TokenCustomizer<JwtEncodingContext> tokenGenerator : this.tokenCustomizers) {
            tokenGenerator.customize(context);
        }
    }
}
