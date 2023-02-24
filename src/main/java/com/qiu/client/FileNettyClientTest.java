package com.qiu.client;


import com.qiu.c.pojo.FileEntity;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * 主程序类
 */
public class FileNettyClientTest {
    public static void connect(int port, String host, final FileEntity fileUploadFile) throws Exception{
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            // 客户端启动时的初始化操作
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY,true)
                    .handler(new FileNettyClientInitializer(fileUploadFile));
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            channelFuture.channel().closeFuture().sync();
        }catch (Exception e){
            System.out.println(e);
        }finally {
            eventLoopGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        int port = 9999;
        if(args != null && args.length > 0){
            port = Integer.valueOf(args[0]);
        }

        FileEntity sendFile = new FileEntity();
        //File file = new File("d:/doctor-2022.h264");
        String fileName = "d:/doctor-2022.h264";
        sendFile.setFileName(fileName);
        sendFile.setStart(0);
        connect(port,"127.0.0.1",sendFile);
    }
}

