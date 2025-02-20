package com.demo.user.entity.repository;

import com.demo.user.config.DatabaseConfig;
import com.demo.user.repository.JpaUserRepository;
import com.demo.user.repository.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@DataJpaTest
@Import(
        value = {
                DatabaseConfig.class
        }
)
class JpaUserRepositoryTest {
    @Autowired
    private JpaUserRepository userRepository;

    @Test
    @DisplayName("아이디 중복 체크 - 중복 없을때")
    public void test1() {
        boolean result = userRepository.existsByUsername("admin");
        Assertions.assertFalse(result);
    }

    @Test
    @DisplayName("아이디 중복 체크 - 중복 있을떄")
    public void test() {
        userRepository.saveAndFlush(User.builder()
                .username("admin")
                .password("{noop}test")
                .email("admin@test.com")
                .firstName("admin")
                .lastName("test")
                .build()
        );
        boolean result = userRepository.existsByUsername("admin");
        Assertions.assertTrue(result);
    }
}