package com.demo.user.web;

import com.demo.common.web.MessageResponse;
import com.demo.user.service.UserRegisterService;
import com.demo.user.web.dto.UserRegisterParam;
import com.demo.user.web.dto.UserRegisterRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.function.Function;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user")
@RestController
public class UserController {
    private final UserRegisterService registerService;

    private final Function<UserRegisterRequest, UserRegisterParam> paramMapper;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping
    public MessageResponse createUser(@Validated @RequestBody UserRegisterRequest request) {
        registerService.apply(paramMapper.apply(request));
        return MessageResponse.ok()
                .message("User registered successfully")
                .build();
    }
}
