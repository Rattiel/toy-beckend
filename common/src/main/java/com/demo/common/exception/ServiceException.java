package com.demo.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public class ServiceException extends RuntimeException {
    private final HttpStatus status;

    public ServiceException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }
}
