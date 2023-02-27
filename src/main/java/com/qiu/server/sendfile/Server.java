package com.qiu.server.sendfile;

import com.qiu.client.SendFile;
import com.qiu.server.sendfile.pojo.FileBody;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

/**
 * @version 1.0
 * @Author:qiu
 * @Description 服务端向客户端发送文件
 * @Date 15:51 2023/2/27
 **/
public class Server {
    static void init(FileBody fileBody){
        EventLoopGroup work = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(work);
        bootstrap.channel(NioDatagramChannel.class);
        bootstrap.handler(new ChannelInitializer() {
            @Override
            protected void initChannel(Channel channel) throws Exception {
                channel.pipeline().addLast(new ServerHandler(fileBody));
            }
        });
        try {
            ChannelFuture future = bootstrap.bind(9999).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            work.shutdownGracefully();
        }

    }
    public static void main(String[] args) {
        FileBody fileBody = new FileBody("1.txt", "d:/");
        init(fileBody);
    }
}
