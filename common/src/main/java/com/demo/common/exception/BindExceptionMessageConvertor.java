package com.demo.common.exception;

import org.springframework.lang.Nullable;
import org.springframework.validation.BindException;

import java.util.function.Function;

public class BindExceptionMessageConvertor implements Function<Throwable, String> {
    @Nullable
    @Override
    public String apply(Throwable throwable) {
        if (throwable instanceof BindException bindException) {
            return bindException.getAllErrors()
                    .getFirst()
                    .getDefaultMessage();
        }
        return null;
    }
}
