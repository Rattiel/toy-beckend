package com.demo.user.web;

import com.demo.common.web.MessageResponse;
import com.demo.user.repository.dto.UserPreview;
import com.demo.user.web.dto.UserFindRequest;
import com.demo.user.web.dto.UserRegisterParam;
import com.demo.user.web.dto.UserRegisterRequest;
import com.demo.user.web.service.WebUserService;
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
    private final WebUserService userService;

    private final Function<UserRegisterRequest, UserRegisterParam> paramMapper;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping
    public MessageResponse createUser(@Validated @RequestBody UserRegisterRequest request) {
        userService.register(paramMapper.apply(request));
        return MessageResponse.ok()
                .message("User registered successfully")
                .build();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public UserPreview findUser(@Validated @RequestBody UserFindRequest request) {
        return userService.findByUsername(request.getUsername());
    }
}
