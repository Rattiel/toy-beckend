package com.demo.auth.authorization.mfa;

import com.demo.auth.authorization.MfaVerificationMethod;

public interface MfaDetails {
    MfaVerificationMethod getVerificationMethod();

    String getVerificationAddress();
}
