package com.demo.common.web;

import com.demo.common.exception.ExceptionMapper;
import com.demo.common.exception.ServiceException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

import java.util.Locale;

@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@Setter
@Component
public class ReactiveExceptionHandler implements WebExceptionHandler {
    private ExceptionMapper exceptionMapper;

    private MessageSourceAccessor messageSourceAccessor;

    private ObjectMapper objectMapper = new ObjectMapper();

    public ReactiveExceptionHandler(ExceptionMapper exceptionMapper) {
        this.exceptionMapper = exceptionMapper;
        this.messageSourceAccessor = new MessageSourceAccessor(
                new ErrorMassageSource(),
                LocaleContextHolder.getLocale()
        );
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messageSourceAccessor = new MessageSourceAccessor(
                messageSource,
                LocaleContextHolder.getLocale()
        );
    }

    @NotNull
    @Override
    public Mono<Void> handle(@NotNull ServerWebExchange exchange, @NotNull Throwable throwable) {
        ServiceException exception = exceptionMapper.apply(throwable);
        exchange.getResponse().setStatusCode(
                HttpStatus.valueOf(exception.getStatus())
        );
        MessageResponse message = MessageResponse.builder()
                .status(exception.getStatus())
                .message(exception.getMessage())
                .messageDecoder(this::decodeMessage)
                .build();
        byte[] body = toBody(message);
        return exchange.getResponse()
                .writeWith(
                        Mono.just(exchange.getResponse().bufferFactory().wrap(body))
                );
    }

    private byte[] toBody(MessageResponse message) {
        try {
            return objectMapper.writeValueAsBytes(message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String decodeMessage(String code) {
        Locale locale = LocaleContextHolder.getLocale();
        return this.messageSourceAccessor.getMessage(code, locale);
    }
}
