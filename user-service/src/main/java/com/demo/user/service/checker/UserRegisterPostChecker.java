package com.demo.user.service.checker;

import com.demo.user.repository.JpaUserRepository;
import com.demo.user.web.dto.UserRegisterParam;
import lombok.RequiredArgsConstructor;
import org.springframework.util.Assert;

import java.util.function.Consumer;

@RequiredArgsConstructor
public class UserRegisterPostChecker implements Consumer<UserRegisterParam> {
    private final JpaUserRepository jpaUserRepository;

    @Override
    public void accept(UserRegisterParam param) {
        boolean existedUsername = jpaUserRepository.existsByEmail(param.getUsername());
        Assert.isTrue(!existedUsername, "Username already exists");

        boolean existedEmail = jpaUserRepository.existsByEmail(param.getEmail());
        Assert.isTrue(!existedEmail, "Email already exists");

        boolean existedPhone = jpaUserRepository.existsByPhone(param.getPhone());
        Assert.isTrue(!existedPhone, "Phone already exists");
    }
}
