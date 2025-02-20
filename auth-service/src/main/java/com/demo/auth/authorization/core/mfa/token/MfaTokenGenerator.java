package com.demo.auth.authorization.core.mfa.token;

import com.demo.auth.authorization.core.mfa.MfaDetails;

@FunctionalInterface
public interface MfaTokenGenerator {
    MfaToken generate(MfaDetails details);

    default void setTokenLifetime(int tokenLifetime) {

    }

    default int getTokenLifetime() {
        return 3000;
    }
}
