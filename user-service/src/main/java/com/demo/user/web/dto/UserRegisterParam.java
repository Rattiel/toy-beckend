package com.demo.user.web.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserRegisterParam {
    private String username;

    private String password;

    private String email;

    private String firstName;

    private String lastName;
}
