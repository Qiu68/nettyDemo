package com.qiu.s.tcp;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

public class ReceiveFileServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline().addLast(new ObjectEncoder());
//        socketChannel.pipeline().addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.weakCachingConcurrentResolver(null)));
        socketChannel.pipeline().addLast(new FixedLengthFrameDecoder(1024));
        //添加自定义Handler处理器，处理文件
        socketChannel.pipeline().addLast(new ReceiveFileTcpHandler());

    }
}
