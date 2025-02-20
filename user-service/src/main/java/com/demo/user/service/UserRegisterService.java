package com.demo.user.service;

import com.demo.user.converter.UserDetailsConverter;
import com.demo.user.repository.JpaUserRepository;
import com.demo.user.repository.entity.User;
import com.demo.user.service.checker.UserRegisterPostChecker;
import com.demo.user.service.checker.UserRegisterPreChecker;
import com.demo.user.web.dto.UserRegisterParam;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Consumer;
import java.util.function.Function;

@Slf4j
@Setter
@Service
public class UserRegisterService implements UserService<UserRegisterParam, UserDetails> {
    private final JpaUserRepository userRepository;

    private final Function<UserRegisterParam, User> userConvertor;

    private Function<User, UserDetails> userDetailsConvertor = new UserDetailsConverter();

    private Consumer<UserRegisterParam> preChecker;

    private Consumer<UserRegisterParam> postChecker;

    public UserRegisterService(JpaUserRepository userRepository, Function<UserRegisterParam, User> userConvertor) {
        this.userRepository = userRepository;
        this.userConvertor = userConvertor;
        this.preChecker = new UserRegisterPreChecker();
        this.postChecker = new UserRegisterPostChecker(this.userRepository);
    }

    @Transactional
    @Override
    public UserDetails apply(UserRegisterParam param) {
        this.preChecker.accept(param);
        log.debug("Register param preChecker passed.");

        User user = userConvertor.apply(param);
        log.debug("Register param converted to User.");

        this.postChecker.accept(param);
        log.debug("Register param postChecker passed.");

        this.userRepository.saveAndFlush(user);
        log.debug("User saved to database.");

        return userDetailsConvertor.apply(user);
    }
}
