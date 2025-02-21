package com.demo.user;

import io.grpc.Channel;

public interface ChannelFactory {
    Channel getChannel(String channelName);
}
