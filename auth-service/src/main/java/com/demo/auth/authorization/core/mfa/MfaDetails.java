package com.demo.auth.authorization.core.mfa;

public interface MfaDetails {
    MfaVerificationMethod getVerificationMethod();

    String getVerificationAddress();
}
