package com.demo.auth.authorization.core.mfa.token;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
@Getter
public class MfaToken implements Serializable {
    private final String verificationMethod;

    private final String verificationAddress;

    private final String code;

    private final LocalDateTime expiresAt;
}
