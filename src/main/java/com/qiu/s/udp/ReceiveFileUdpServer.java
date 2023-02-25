package com.qiu.s.udp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.FixedRecvByteBufAllocator;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

public class ReceiveFileUdpServer {
    public static void main(String[] args)   {
        EventLoopGroup group = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group);
        bootstrap.channel(NioDatagramChannel.class);
        //设置接收数据的最大字节数
        bootstrap.option(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(61440));
        //设置udp socket缓冲 2m
        bootstrap.option(ChannelOption.SO_RCVBUF, 1024 * 1048*2);
        bootstrap.handler(new ReceiveFileUdpInitializer());
        try {
            ChannelFuture sync = bootstrap.bind(8888).sync();
            sync.channel().closeFuture().sync();
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }

        finally {
            group.shutdownGracefully();
        }

    }
}
