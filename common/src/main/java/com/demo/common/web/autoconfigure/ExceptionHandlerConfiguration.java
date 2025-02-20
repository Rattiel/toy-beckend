package com.demo.common.web.autoconfigure;

import com.demo.common.exception.BindExceptionMessageConvertor;
import com.demo.common.exception.ExceptionMapper;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.method.MethodValidationException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.context.request.async.AsyncRequestNotUsableException;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Configuration(proxyBeanMethods = false)
public class ExceptionHandlerConfiguration {
    @Bean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    @ConditionalOnMissingBean
    public ExceptionMapper exceptionMapper() {
        return ExceptionMapper.builder()
                .addMapping(MissingServletRequestParameterException.class, HttpStatus.BAD_REQUEST)
                .addMapping(MissingServletRequestPartException.class, HttpStatus.BAD_REQUEST)
                .addMapping(ServletRequestBindingException.class, HttpStatus.BAD_REQUEST)
                .addMapping(TypeMismatchException.class, HttpStatus.BAD_REQUEST)
                .addMapping(HttpMessageNotReadableException.class, HttpStatus.BAD_REQUEST)
                .addMapping(MethodArgumentNotValidException.class, HttpStatus.BAD_REQUEST)
                .addMapping(HandlerMethodValidationException.class, HttpStatus.BAD_REQUEST)
                .addMapping(NoHandlerFoundException.class, HttpStatus.NOT_FOUND)
                .addMapping(NoResourceFoundException.class, HttpStatus.NOT_FOUND)
                .addMapping(HttpRequestMethodNotSupportedException.class, HttpStatus.METHOD_NOT_ALLOWED)
                .addMapping(HttpMediaTypeNotAcceptableException.class, HttpStatus.NOT_ACCEPTABLE)
                .addMapping(HttpMediaTypeNotSupportedException.class, HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .addMapping(MethodValidationException.class, HttpStatus.INTERNAL_SERVER_ERROR)
                .addMapping(HttpMessageNotWritableException.class, HttpStatus.INTERNAL_SERVER_ERROR)
                .addMapping(ConversionNotSupportedException.class, HttpStatus.INTERNAL_SERVER_ERROR)
                .addMapping(MissingPathVariableException.class, HttpStatus.INTERNAL_SERVER_ERROR)
                .addMapping(AsyncRequestTimeoutException.class, HttpStatus.SERVICE_UNAVAILABLE)
                .addMapping(AsyncRequestNotUsableException.class, HttpStatus.SERVICE_UNAVAILABLE)
                .addMessageConverter(new BindExceptionMessageConvertor())
                .build();
    }

    @Bean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    @ConditionalOnMissingBean
    public ExceptionMapper reactiveExceptionMapper() {
        return ExceptionMapper.builder()
                .addMapping(MissingServletRequestParameterException.class, HttpStatus.BAD_REQUEST)
                .addMapping(MissingServletRequestPartException.class, HttpStatus.BAD_REQUEST)
                .addMapping(ServletRequestBindingException.class, HttpStatus.BAD_REQUEST)
                .addMapping(TypeMismatchException.class, HttpStatus.BAD_REQUEST)
                .addMapping(HttpMessageNotReadableException.class, HttpStatus.BAD_REQUEST)
                .addMapping(MethodArgumentNotValidException.class, HttpStatus.BAD_REQUEST)
                .addMapping(HandlerMethodValidationException.class, HttpStatus.BAD_REQUEST)
                .addMapping(HttpRequestMethodNotSupportedException.class, HttpStatus.METHOD_NOT_ALLOWED)
                .addMapping(HttpMediaTypeNotAcceptableException.class, HttpStatus.NOT_ACCEPTABLE)
                .addMapping(HttpMediaTypeNotSupportedException.class, HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .addMapping(MethodValidationException.class, HttpStatus.INTERNAL_SERVER_ERROR)
                .addMapping(HttpMessageNotWritableException.class, HttpStatus.INTERNAL_SERVER_ERROR)
                .addMapping(ConversionNotSupportedException.class, HttpStatus.INTERNAL_SERVER_ERROR)
                .addMapping(MissingPathVariableException.class, HttpStatus.INTERNAL_SERVER_ERROR)
                .addMapping(AsyncRequestTimeoutException.class, HttpStatus.SERVICE_UNAVAILABLE)
                .addMapping(AsyncRequestNotUsableException.class, HttpStatus.SERVICE_UNAVAILABLE)
                .addMapping(org.springframework.web.reactive.resource.NoResourceFoundException.class, HttpStatus.NOT_FOUND)
                .addMapping(org.springframework.web.reactive.function.UnsupportedMediaTypeException.class, HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .addMessageConverter(new BindExceptionMessageConvertor())
                .build();
    }
}
