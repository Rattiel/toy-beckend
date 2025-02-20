package com.demo.auth.authorization.mfa;

import com.demo.auth.authorization.mfa.token.MfaToken;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Setter
@Getter
public class MfaNeedAuthenticationToken extends UsernamePasswordAuthenticationToken {
    private static final GrantedAuthority MFA_AUTHORITY = new SimpleGrantedAuthority("SCOPE_MFA_AUTHENTICATION_NEED");

    private final Collection<? extends GrantedAuthority> principalAuthorities;

    private MfaToken token;

    public MfaNeedAuthenticationToken(Object principal, Object credentials, MfaToken token) {
        super(principal, credentials, List.of(MFA_AUTHORITY));
        this.principalAuthorities = Collections.emptyList();
        this.token = token;
        setAuthenticated(false);
    }

    public MfaNeedAuthenticationToken(
            Object principal,
            Object credentials,
            Collection<? extends GrantedAuthority> authorities,
            MfaToken token
    ) {
        super(principal, credentials, List.of(MFA_AUTHORITY));
        this.principalAuthorities = authorities;
        this.token = token;
        setAuthenticated(false);
    }
}
