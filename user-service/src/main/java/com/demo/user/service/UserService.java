package com.demo.user.service;

import java.util.function.Function;

@FunctionalInterface
public interface UserService<T, R> extends Function<T, R> {
    default boolean isSupport(Class<?> clazz) {
        return false;
    }
}
