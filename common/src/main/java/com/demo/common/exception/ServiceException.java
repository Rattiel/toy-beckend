package com.demo.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ServiceException extends RuntimeException {
    private final int status;

    public ServiceException(int status, String message) {
        super(message);
        this.status = status;
    }
}
