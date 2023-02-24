package com.qiu.client;


import com.qiu.c.pojo.FileEntity;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

/**
 * 自定义初始化器
 */
public class FileNettyClientInitializer extends ChannelInitializer<SocketChannel> {

    FileEntity sendFile;

    public FileNettyClientInitializer(FileEntity sendFile){
        this.sendFile = sendFile;
    }

    @Override
    protected void initChannel(SocketChannel sc) throws Exception {
        ChannelPipeline pipeline = sc.pipeline();
        pipeline.addLast(new ObjectEncoder());
        // ObjectDecoder：Object编解码器
        // 使用weakCachingConcurrentResolver创建线程安全的WeakReferenceMap对类加载器进行缓存，
        // 它支持多线程并发访问，当虚拟机内存不足时，会释放缓存中的内存，防止内存泄漏。
        pipeline.addLast(new ObjectDecoder(ClassResolvers.weakCachingConcurrentResolver(null)));
        // 自定义处理器
        pipeline.addLast(new FileNettyClientHandler(sendFile));
    }
}

