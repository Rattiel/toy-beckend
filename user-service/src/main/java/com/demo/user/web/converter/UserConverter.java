package com.demo.user.web.converter;

import com.demo.user.repository.entity.MfaVerificationMethod;
import com.demo.user.repository.entity.User;
import com.demo.user.web.dto.UserRegisterParam;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Function;

@RequiredArgsConstructor
@Component
public class UserConverter implements Function<UserRegisterParam, User> {
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    @Override
    public User apply(UserRegisterParam param) {
        String encodedPassword = passwordEncoder.encode(param.getPassword());
        return User.builder()
                .username(param.getUsername())
                .password(encodedPassword)
                .email(param.getEmail())
                .phone(param.getPhone())
                .firstName(param.getFirstName())
                .lastName(param.getLastName())
                .mfaEnabled(false)
                .mfaVerificationMethod(MfaVerificationMethod.NONE)
                .build();
    }
}
