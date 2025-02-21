package com.demo.user;

import com.demo.common.exception.ServiceException;
import com.demo.user.rpc.UserProto;
import com.demo.user.rpc.UserServiceGrpc;
import io.grpc.Channel;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
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
        UserProto.User user;
        try {
            user = stub.findByUsername(request);
        } catch (StatusRuntimeException e) {
            if (e.getStatus().getCode() == Status.Code.NOT_FOUND) {
                throw new UsernameNotFoundException(e.getMessage());
            }
            throw new ServiceException(503, e.getMessage());
        }
        return this.converter.apply(user);
    }
}
