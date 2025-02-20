package com.demo.auth.web.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.Collection;

public class InmemoryUserService implements UserService {
    private final InMemoryUserDetailsManager delegate;

    public InmemoryUserService() {
        this.delegate = new InMemoryUserDetailsManager();
    }

    public InmemoryUserService(Collection<UserDetails> users) {
        this.delegate = new InMemoryUserDetailsManager();
        for (UserDetails user : users) {
            this.delegate.createUser(user);
        }
    }

    public InmemoryUserService(UserDetails... users) {
        this.delegate = new InMemoryUserDetailsManager();
        for (UserDetails user : users) {
            this.delegate.createUser(user);
        }
    }

    @Override
    public UserDetails updatePassword(UserDetails user, String newPassword) {
        return delegate.updatePassword(user, newPassword);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return delegate.loadUserByUsername(username);
    }
}
