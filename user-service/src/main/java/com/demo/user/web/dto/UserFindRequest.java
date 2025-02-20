package com.demo.user.web.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserFindRequest {
    @NotEmpty(message = "username cannot be empty")
    private String username;
}
