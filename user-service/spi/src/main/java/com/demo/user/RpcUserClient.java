package com.demo.user;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface RpcUserClient {
    UserDetails findByUsername(String username) throws UsernameNotFoundException;
}
