package com.demo.user.service.checker;

import com.demo.user.web.dto.UserRegisterParam;
import org.springframework.util.Assert;

import java.util.function.Consumer;

public class UserRegisterPreChecker implements Consumer<UserRegisterParam> {
    @Override
    public void accept(UserRegisterParam param) {
        Assert.hasText(param.getUsername(), "Username cannot be empty");

        Assert.hasText(param.getPassword(), "Password cannot be empty");

        Assert.hasText(param.getEmail(), "Email cannot be empty");

        Assert.hasText(param.getPhone(), "Phone cannot be empty");

        Assert.hasText(param.getFirstName(), "First name cannot be empty");

        Assert.hasText(param.getLastName(), "Last name cannot be empty");
    }
}
