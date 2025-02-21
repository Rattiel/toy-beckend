package com.demo.user.web.converter;

import com.demo.user.web.dto.UserRegisterParam;
import com.demo.user.web.dto.UserRegisterRequest;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class UserParamConverter implements Function<UserRegisterRequest, UserRegisterParam> {
    @Override
    public UserRegisterParam apply(UserRegisterRequest request) {
        return UserRegisterParam.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .email(request.getEmail())
                .phone(request.getPhone())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .build();
    }
}
