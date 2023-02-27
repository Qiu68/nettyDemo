package com.qiu.client.receive;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;

/**
 * @version 1.0
 * @Author:qiu
 * @Description
 * @Date 16:19 2023/2/27
 **/
public class Client {
    static void init(){
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group);
        bootstrap.channel(NioDatagramChannel.class);
        bootstrap.option(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(61440));
        //设置udp socket缓冲 2m
        bootstrap.option(ChannelOption.SO_RCVBUF, 1024 * 1048*2);
        bootstrap.handler(new ChannelInitializer() {
            @Override
            protected void initChannel(Channel channel) throws Exception {
                channel.pipeline().addLast(new ClientReceiveFile());
            }
        });
        try {
            ChannelFuture connect = bootstrap.connect("127.0.0.1", 9999).sync();
            connect.channel().writeAndFlush("hello server!");
            connect.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        init();
    }
}
