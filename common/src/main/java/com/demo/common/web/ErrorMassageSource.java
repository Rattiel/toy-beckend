package com.demo.common.web;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ResourceBundleMessageSource;

public class ErrorMassageSource extends ResourceBundleMessageSource {
    public ErrorMassageSource() {
        this.setBasename("classpath:error_messages");
        this.setUseCodeAsDefaultMessage(true);
        this.setDefaultEncoding("UTF-8");
    }

    public static MessageSourceAccessor getAccessor() {
        return new MessageSourceAccessor(new ErrorMassageSource());
    }
}
