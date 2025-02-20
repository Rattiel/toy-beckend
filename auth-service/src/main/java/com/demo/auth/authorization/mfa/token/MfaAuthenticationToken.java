package com.demo.auth.authorization.mfa.token;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.util.Assert;

import java.util.Objects;

@Getter
public class MfaAuthenticationToken extends AbstractAuthenticationToken {
    private final Object principal;

    @Getter
    private final String code;

    public MfaAuthenticationToken(Object principal, String code) {
        super(null);
        this.principal = principal;
        this.code = code;
        super.setAuthenticated(false);
    }

    @Override
    public void setAuthenticated(boolean authenticated) {
        Assert.isTrue(!authenticated, "Cannot set this token to trusted");
    }

    @Override
    public Object getCredentials() {
        return this.code;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        if (!super.equals(object)) return false;
        MfaAuthenticationToken that = (MfaAuthenticationToken) object;
        return Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), code);
    }
}
