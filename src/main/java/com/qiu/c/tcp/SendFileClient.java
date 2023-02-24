package com.qiu.c.tcp;

import com.qiu.c.pojo.FileEntity;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;



public class SendFileClient {

    public static void connect(String host, int port, FileEntity file)  {
        //创建线程组用来发送文件
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            //创建bootstrap启动器
            Bootstrap bootstrap = new Bootstrap();
            //将eventLoopGroup线程组加入组，会一直循环
            bootstrap.group(eventLoopGroup);
            //设置channel
            bootstrap.channel(NioSocketChannel.class);
            //给channel增加配置
            bootstrap.option(ChannelOption.TCP_NODELAY, true);
            //
            bootstrap.handler(new SendFileInitializer(file));
            //阻塞监听
            ChannelFuture sync = bootstrap.connect(host, port).sync();
            //关闭监听，阻塞等待服务器数据
            sync.channel().closeFuture().sync();
        }
        catch (InterruptedException e){
            System.out.println(e);
        }
        finally {
            eventLoopGroup.shutdownGracefully();
        }

    }
    public static void main(String[] args) {

        FileEntity file = new FileEntity();
        file.setFileName("d:/doctor-2022.h264");
        file.setStart(0);
        connect("127.0.0.1",9999,file);
    }
}
