package com.demo.auth.authorization.core.mfa;

import org.springframework.security.core.AuthenticationException;

public class InvalidMfaRequestException extends AuthenticationException {
    public InvalidMfaRequestException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public InvalidMfaRequestException(String msg) {
        super(msg);
    }
}
