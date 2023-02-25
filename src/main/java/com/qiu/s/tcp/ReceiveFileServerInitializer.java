package com.qiu.s.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

public class ReceiveFileServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        //ByteBuf buf = ByteBufAllocator.DEFAULT.buffer(0,64*1024);
        socketChannel.pipeline().addLast(new ObjectEncoder());
        System.out.println(socketChannel);
//        socketChannel.pipeline().addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.weakCachingConcurrentResolver(null)));
        socketChannel.pipeline().addLast(new FixedLengthFrameDecoder(640));
        //添加自定义Handler处理器，处理文件
        socketChannel.pipeline().addLast(new ReceiveFileTcpHandler());

    }
}
