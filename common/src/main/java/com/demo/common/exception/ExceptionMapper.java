package com.demo.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Slf4j
public class ExceptionMapper implements Function<Throwable, ServiceException> {
    private final Map<Class<? extends Throwable>, HttpStatus> mappings;

    private List<Function<Throwable, String>> messageConverters = new ArrayList<>();

    public ExceptionMapper(Map<Class<? extends Throwable>, HttpStatus> mappings) {
        this.mappings = mappings;
    }

    public ExceptionMapper(
            Map<Class<? extends Throwable>, HttpStatus> mappings,
            List<Function<Throwable, String>> messageConverters) {
        this.mappings = mappings;
        this.messageConverters = messageConverters;
    }

    @Override
    public ServiceException apply(Throwable exception) {
        if (exception instanceof ServiceException serviceException) {
            return serviceException;
        }
        for (Map.Entry<Class<? extends Throwable>, HttpStatus> mapping : mappings.entrySet()) {
            if (mapping.getKey().isInstance(exception)) {
                return new ServiceException(
                        mapping.getValue(),
                        getMessage(exception)
                );
            }
        }
        log.error(
                "ExceptionMapper cannot match Exception: {}",
                exception.getClass().getName()
        );
        return new ServiceException(
                HttpStatus.SERVICE_UNAVAILABLE,
                getMessage(exception)
        );
    }

    private String getMessage(Throwable throwable) {
        for (Function<Throwable, String> messageConverter : messageConverters) {
            String message = messageConverter.apply(throwable);
            if (message != null) {
                return message;
            }
        }
        return throwable.getMessage();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private final Map<Class<? extends Throwable>, HttpStatus> mappings = new HashMap<>();

        private final List<Function<Throwable, String>> messageConverters = new ArrayList<>();

        private Builder() {
        }

        public Builder addMappings(Map<Class<? extends Throwable>, HttpStatus> mappings) {
            this.mappings.putAll(mappings);
            return this;
        }

        public Builder addMapping(Class<? extends Throwable> throwable, HttpStatus status) {
            this.mappings.put(throwable, status);
            return this;
        }

        public Builder addMessageConverters(List<Function<Throwable, String>> messageConverters) {
            this.messageConverters.addAll(messageConverters);
            return this;
        }

        public Builder addMessageConverter(Function<Throwable, String> messageConverter) {
            this.messageConverters.add(messageConverter);
            return this;
        }

        public ExceptionMapper build() {
            return new ExceptionMapper(this.mappings, this.messageConverters);
        }
    }
}
