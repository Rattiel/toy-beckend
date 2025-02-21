package com.demo.auth.authorization.core.mfa.token;

import com.demo.auth.authorization.core.mfa.InvalidMfaRequestException;
import com.demo.auth.authorization.core.mfa.MfaNeedAuthenticationToken;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.util.Assert;

@Slf4j
@Setter
public class MfaTokenAuthenticationProvider implements AuthenticationProvider {
    private SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Assert.isInstanceOf(
                MfaAuthenticationToken.class,
                authentication,
                "Only MfaAuthenticationToken is supported"
        );
        MfaAuthenticationToken request = (MfaAuthenticationToken) authentication;
        SecurityContext context = securityContextHolderStrategy.getContext();
        if (!(context.getAuthentication() instanceof MfaNeedAuthenticationToken nowAuthentication)) {
            throw new InvalidMfaRequestException("MFA Authentication not supported");
        }
        if (!nowAuthentication.getToken().getCode().equals(request.getCode())) {
            throw new BadCredentialsException("Invalid code");
        }
        return nowAuthentication.getBeforeAuthentication();
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return MfaAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
