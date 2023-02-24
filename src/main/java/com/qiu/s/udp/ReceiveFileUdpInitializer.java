package com.qiu.s.udp;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.DatagramChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;


public class ReceiveFileUdpInitializer extends ChannelInitializer<DatagramChannel> {

    @Override
    protected void initChannel(DatagramChannel datagramChannel) {
       //datagramChannel.pipeline().addLast(new ObjectEncoder());
////        socketChannel.pipeline().addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.weakCachingConcurrentResolver(null)));
//     datagramChannel.pipeline().addLast(new FixedLengthFrameDecoder(2));
//        //添加自定义Handler处理器，处理文件
        //datagramChannel.pipeline().addLast(new FixedLengthFrameDecoder(2));
        datagramChannel.pipeline().addLast(new ReceiveUdpHandler());
    }
}
