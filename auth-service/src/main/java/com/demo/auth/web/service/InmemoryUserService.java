package com.demo.auth.web.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class InmemoryUserService implements UserService {
    private final Map<String, UserDetails> memory = new HashMap<>();

    public InmemoryUserService() {
    }

    public InmemoryUserService(Collection<UserDetails> users) {
        for (UserDetails user : users) {
            this.memory.put(user.getUsername().toLowerCase(), user);
        }
    }

    public InmemoryUserService(UserDetails... users) {
        for (UserDetails user : users) {
            this.memory.put(user.getUsername().toLowerCase(), user);
        }
    }

    @Override
    public UserDetails updatePassword(UserDetails user, String newPassword) {
        log.info("UpdatePassword not implemented.");
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.memory.get(username.toLowerCase());
    }
}
