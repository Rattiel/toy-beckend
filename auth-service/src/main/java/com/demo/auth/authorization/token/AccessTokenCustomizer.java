
package com.demo.auth.authorization.token;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import static org.springframework.security.oauth2.server.authorization.OAuth2TokenType.ACCESS_TOKEN;

@Slf4j
public class AccessTokenCustomizer implements OAuth2TokenCustomizer<JwtEncodingContext> {
    @Override
    public void customize(JwtEncodingContext context) {
        if (ACCESS_TOKEN.equals(context.getTokenType())) {
            context.getClaims().claims((claims) -> {
                claims.get("scope");

                context.getPrincipal();

                claims.put("test", "test");
            });
        }
    }
}
