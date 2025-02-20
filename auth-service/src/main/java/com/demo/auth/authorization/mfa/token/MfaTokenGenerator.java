package com.demo.auth.authorization.mfa.token;

import com.demo.auth.authorization.mfa.MfaDetails;

@FunctionalInterface
public interface MfaTokenGenerator {
    MfaToken generate(MfaDetails details);

    default void setTokenLifetime(int tokenLifetime) {

    }

    default int getTokenLifetime() {
        return 3000;
    }
}
