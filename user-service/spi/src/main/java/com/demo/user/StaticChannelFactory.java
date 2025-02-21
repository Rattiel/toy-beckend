package com.demo.user;

import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.stereotype.Component;

@Component
public class StaticChannelFactory implements ChannelFactory {
    @Override
    public Channel getChannel(String channelName) {
        return ManagedChannelBuilder.forTarget(channelName)
                .usePlaintext()
                .build();
    }
}
