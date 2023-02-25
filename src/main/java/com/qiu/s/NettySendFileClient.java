package com.qiu.s;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

/**
 * @version 1.0
 * @Author:qiu
 * @Description
 * @Date 10:45 2023/2/25
 **/
public class NettySendFileClient {

    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup loopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(loopGroup);
        bootstrap.channel(NioDatagramChannel.class);
        //设置缓冲区 20m
        bootstrap.option(ChannelOption.SO_SNDBUF, 20 * 1024 * 1024);
        bootstrap.handler(new ChannelInitializer<NioDatagramChannel>() {
            @Override
            protected void initChannel(NioDatagramChannel nioDatagramChannel) throws Exception {
                nioDatagramChannel.pipeline().addLast(new NettySendFileHandler());
            }
        });
        ChannelFuture bind = bootstrap.bind("127.0.0.1", 0).sync();
        bind.channel().closeFuture().sync();
        System.out.println("传输结束");
    }
}
