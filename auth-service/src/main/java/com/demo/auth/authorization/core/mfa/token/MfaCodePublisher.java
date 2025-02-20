package com.demo.auth.authorization.core.mfa.token;

@FunctionalInterface
public interface MfaCodePublisher {
    void send(MfaToken token);

    default boolean support(String method) {
        return true;
    }
}
