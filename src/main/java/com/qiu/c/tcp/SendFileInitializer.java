package com.qiu.c.tcp;

import com.qiu.c.pojo.FileEntity;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

public class SendFileInitializer extends ChannelInitializer<SocketChannel> {

    FileEntity file;

    public SendFileInitializer(FileEntity file){
        this.file = file;
    }


    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline().addLast(new ObjectEncoder());
        socketChannel.pipeline().addLast(new ObjectDecoder(ClassResolvers.weakCachingConcurrentResolver(null)));
        //自定义处理器
        socketChannel.pipeline().addLast(new SendFileHandler(file));
    }
}
