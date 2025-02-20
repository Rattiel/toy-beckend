package com.demo.auth.authorization;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum MfaVerificationMethod {
    NONE("NONE"),
    EMAIL("EMAIL"),
    PHONE("PHONE");
    private final String value;
}
