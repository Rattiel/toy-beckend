package com.demo.user.config;

import com.example.common.security.SaltPasswordEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@Configuration(proxyBeanMethods = false)
public class DefaultSecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new SaltPasswordEncoder();
    }
}
