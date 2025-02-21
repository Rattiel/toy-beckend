package com.demo.auth.authorization.core.mfa;

import com.demo.auth.authorization.core.mfa.token.MfaToken;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

@Setter
@Getter
public class MfaNeedAuthenticationToken extends UsernamePasswordAuthenticationToken {
    private static final GrantedAuthority MFA_AUTHORITY = new SimpleGrantedAuthority("SCOPE_MFA_AUTHENTICATION_NEED");

    private final Authentication beforeAuthentication;

    private MfaToken token;

    public MfaNeedAuthenticationToken(Authentication beforeAuthentication, MfaToken token) {
        super(beforeAuthentication.getPrincipal(), beforeAuthentication.getCredentials(), List.of(MFA_AUTHORITY));
        this.beforeAuthentication = beforeAuthentication;
        this.token = token;
        setAuthenticated(false);
    }
}
