package com.demo.auth.config;

import com.demo.auth.web.service.RpcUserService;
import com.demo.user.ChannelFactory;
import com.demo.user.GRpcUserClient;
import com.demo.user.RpcUserClient;
import com.demo.user.StaticChannelFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class GrpcConfig {
    private final static String USER_SERVICE = "localhost:8090";

    @Bean
    public ChannelFactory channelFactory() {
        return new StaticChannelFactory();
    }

    @Bean
    public RpcUserClient userClient(ChannelFactory channelFactory) {
        return new GRpcUserClient(USER_SERVICE, channelFactory, new RpcUserService.UserDetailsConverter());
    }
}
