package com.qiu.server;


import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

/**
 * 自定义初始化器
 */
public class FileNettyServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        // 可以把ChannelPipeline看成是一个ChandlerHandler的链表,
        // 当需要对Channel进行某种处理的时候，Pipeline负责依次调用每一个Handler进行处理。
        ChannelPipeline pipeline = socketChannel.pipeline();

        // addLast类似Spring的beanFactory给每个bean起名字
        pipeline.addLast(new ObjectEncoder());
        pipeline.addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.weakCachingConcurrentResolver(null)));

        // 增加自定义处理器FileNettyServerHandler，用于实际处理请求，并给出响应
        pipeline.addLast(new FileNettyServerHandler());
    }
}

