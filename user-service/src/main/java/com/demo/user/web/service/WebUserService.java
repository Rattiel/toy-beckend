package com.demo.user.web.service;

import com.demo.user.repository.JpaUserRepository;
import com.demo.user.repository.dto.UserPreview;
import com.demo.user.repository.entity.User;
import com.demo.user.web.dto.UserRegisterParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Optional;
import java.util.function.Function;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class WebUserService {
    private final JpaUserRepository userRepository;

    private final Function<UserRegisterParam, User> userConvertor;

    @Transactional
    public void register(UserRegisterParam param) {
        Assert.notNull(param, "param must not be null");
        User newUser = userConvertor.apply(param);
        userRepository.save(newUser);
    }

    public UserPreview findByUsername(String username) {
        Optional<UserPreview> userPreview = userRepository.findPreviewByUsername(username);
        return userPreview.orElse(null);
    }
}
