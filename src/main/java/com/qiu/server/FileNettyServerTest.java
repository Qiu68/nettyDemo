package com.qiu.server;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 主程序类
 */
public class FileNettyServerTest {
    public static void main(String[] args) throws InterruptedException {
        /**
         * EventLoopGroup：事件循环组，是一个线程池，也是一个死循环，用于不断地接收用户请求；
         * serverGroup：用户监听及建立连接，并把每一个连接抽象为一个channel，最后再将连接交给clientGroup处理；
         * clientGroup：真正的处理连接
         */
        EventLoopGroup serverGroup = new NioEventLoopGroup();
        EventLoopGroup clientGroup = new NioEventLoopGroup();
        try {
            // ServerBootstrap：服务端启动引导类
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            // 1、将serverGroup和clientGroup注册到服务端的Channel上；
            // 2、注册一个服务端的初始化器MyNettyServerInitializer；
            // 3、该初始化器中的initChannel()方法会在连接被注册到Channel后立刻执行；
            // 5、最后将端口号绑定到8080；
            // ChannelFuture用来保存Channel异步操作的结果
            ChannelFuture channelFuture = serverBootstrap.group(serverGroup, clientGroup)//group用来设置循环组
                    .channel(NioServerSocketChannel.class)//channel用来设置服务端的通道实现，ServerSocketChannel是一个可以监听新进来的TCP连接的通道
                    .option(ChannelOption.SO_BACKLOG,1024)//option用来给channel添加配置
                    // childHandler用来设置业务处理类
                    // sync()的作用是“直到连接返回，才会继续后面的执行，否则阻塞当前线程”
                    .childHandler(new FileNettyServerInitializer()).bind(9999).sync();
            channelFuture.channel().closeFuture().sync();
        }catch (Exception e){
            System.out.println(e);
        }finally {
            serverGroup.shutdownGracefully();
            clientGroup.shutdownGracefully();
        }
    }
}
