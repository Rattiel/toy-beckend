package com.demo.user.converter;

import com.demo.user.repository.entity.User;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;

public class UserDetailsConverter implements Function<User, UserDetails> {
    @Override
    public UserDetails apply(User user) {
        return EmptyUserDetails.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .build();
    }

    @Builder
    @Getter
    public static class EmptyUserDetails implements UserDetails {
        private String username;

        private String password;

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return Collections.emptyList();
        }
    }
}
