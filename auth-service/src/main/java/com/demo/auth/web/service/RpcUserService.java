package com.demo.auth.web.service;

import com.demo.auth.authorization.core.Account;
import com.demo.user.RpcUserClient;
import com.demo.user.rpc.UserProto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Slf4j
@RequiredArgsConstructor
@Service
public class RpcUserService implements UserService {
    private final RpcUserClient client;

    @Override
    public UserDetails updatePassword(UserDetails user, String newPassword) {
        log.info("UpdatePassword not implemented.");
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return client.findByUsername(username);
    }

    public static class UserDetailsConverter implements Function<UserProto.User, Account> {
        @Override
        public Account apply(UserProto.User user) {
            return Account.withUsername(user.getUsername())
                    .password(user.getPassword())
                    .email(user.getEmail())
                    .phone(user.getPhone())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .mfaEnable(user.getMfaEnable())
                    .mfaMethod(user.getMfaMethod())
                    .build();
        }
    }
}
