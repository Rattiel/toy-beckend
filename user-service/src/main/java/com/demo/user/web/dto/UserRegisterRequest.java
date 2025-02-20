package com.demo.user.web.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserRegisterRequest {
    @NotEmpty(message = "username cannot be empty")
    private String username;

    @NotEmpty(message = "password cannot be empty")
    private String password;

    @NotEmpty(message = "email cannot be empty")
    private String email;

    @NotEmpty(message = "firstName cannot be empty")
    private String firstName;

    @NotEmpty(message = "lastName cannot be empty")
    private String lastName;
}
