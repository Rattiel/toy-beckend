package com.demo.auth.authorization.core.mfa;

public interface MfaDetails {
    String getVerificationMethod();

    String getVerificationAddress();

    default boolean isEnabled() {
        return this.getVerificationMethod() != null && this.getVerificationAddress() != null;
    }
}
