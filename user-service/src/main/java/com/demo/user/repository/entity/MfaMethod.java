package com.demo.user.repository.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum MfaMethod {
    NONE("NONE"),
    EMAIL("EMAIL"),
    PHONE("PHONE");
    private final String value;
}
