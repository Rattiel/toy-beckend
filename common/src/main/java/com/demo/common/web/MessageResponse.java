package com.demo.common.web;

import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;

import java.util.function.Function;

public record MessageResponse(int status, String message) {
    public static Builder ok() {
        Builder builder = new Builder();
        builder.status(HttpStatus.OK);
        return builder;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private HttpStatus status;
        private String message;
        private Function<String, String> messageDecoder = (message) -> message;

        private Builder() {
        }

        public Builder status(HttpStatus status) {
            Assert.notNull(status, "username cannot be null");
            this.status = status;
            return this;
        }

        public Builder message(String message) {
            Assert.notNull(message, "username cannot be null");
            this.message = message;
            return this;
        }

        public Builder messageDecoder(Function<String, String> messageDecoder) {
            Assert.notNull(messageDecoder, "messageDecoder cannot be null");
            this.messageDecoder = messageDecoder;
            return this;
        }

        public MessageResponse build() {
            String decodedMessage = this.messageDecoder.apply(this.message);
            return new MessageResponse(
                    this.status.value(),
                    decodedMessage
            );
        }
    }
}
