package com.demo.user;

import com.demo.user.rpc.UserProto;
import com.demo.user.rpc.UserServiceGrpc;
import io.grpc.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.function.Function;

@Slf4j
@RequiredArgsConstructor
public class GRpcUserClient implements RpcUserClient {
    private final String serverName;

    private final ChannelFactory factory;

    private final Function<UserProto.User, ? extends UserDetails> converter;

    @Override
    public UserDetails findByUsername(String username) throws UsernameNotFoundException {
        UserProto.UsernameRequest request = UserProto.UsernameRequest.newBuilder()
                .setUsername(username)
                .build();
        Channel channel = this.factory.getChannel(serverName);
        UserServiceGrpc.UserServiceBlockingStub stub = UserServiceGrpc.newBlockingStub(channel);
        UserProto.User user = stub.findByUsername(request);
        return this.converter.apply(user);
    }
}
