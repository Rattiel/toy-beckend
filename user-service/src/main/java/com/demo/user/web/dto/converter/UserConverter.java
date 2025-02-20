package com.demo.user.web.dto.converter;

import com.demo.user.repository.JpaUserRepository;
import com.demo.user.repository.entity.User;
import com.demo.user.web.dto.UserRegisterParam;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.function.Function;

@RequiredArgsConstructor
@Component
public class UserConverter implements Function<UserRegisterParam, User> {
    private final PasswordEncoder passwordEncoder;

    private final JpaUserRepository jpaUserRepository;

    @Transactional(readOnly = true)
    @Override
    public User apply(UserRegisterParam param) {
        checkUsername(param.getUsername());
        checkEmail(param.getEmail());
        checkPassword(param.getPassword());
        String encodedPassword = passwordEncoder.encode(param.getPassword());
        return User.builder()
                .username(param.getUsername())
                .password(encodedPassword)
                .email(param.getEmail())
                .firstName(param.getFirstName())
                .lastName(param.getLastName())
                .build();
    }

    private void checkUsername(String username) {
        boolean exists = jpaUserRepository.existsByUsername(username);
        Assert.isTrue(!exists, "username " + username + " already exists");
    }

    private void checkEmail(String email) {
        boolean exists = jpaUserRepository.existsByEmail(email);
        Assert.isTrue(!exists, "email " + email + " already exists");
    }

    private void checkPassword(String password) {
        Assert.hasLength(password, "password must not be empty");
    }
}
