package com.qiu.s;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultFileRegion;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.InetSocketAddress;


/**
 * @version 1.0
 * @Author:qiu
 * @Description
 * @Date 10:52 2023/2/25
 **/
public class NettySendFileHandler extends SimpleChannelInboundHandler<NioDatagramChannel> {

    static FileInputStream fis;
    static BufferedInputStream bis;
    static long length = 0;
    static int count = 1;

    static {
        try {
            fis = new FileInputStream("d:/569mb.h264");
            bis = new BufferedInputStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        //可以获得fileChannl
        //ctx.write(new DefaultFileRegion())

        System.out.println("开始传输");

        byte[] temp =new  byte[60*1024];


        //开始读取文件
       if (length == -1){
           System.out.println("length == -1 ....");
           ctx.close();
       }





       while (true){
           int readSize = bis.read(temp);
           if (readSize == -1){
               break;
           }
           byte[] byteData = new byte[readSize];
           ByteBuf data = ByteBufAllocator.DEFAULT.buffer(0,60*1024);
           System.arraycopy(temp,0,byteData,0,readSize);
           data.writeBytes(temp);
           DatagramPacket dp = new DatagramPacket(data,
                   new InetSocketAddress("127.0.0.1",8888));
           ctx.writeAndFlush(dp);
           System.out.println("发送次数:"+count++ +"  发送字节数:"+byteData.length);
           Thread.sleep(1);
       }
//
//        ByteBuf data = ByteBufAllocator.DEFAULT.buffer(0,60*1024);
//        data.writeBytes("hello".getBytes(StandardCharsets.UTF_8));
//        DatagramPacket dp = new DatagramPacket(data,
//                new InetSocketAddress("127.0.0.1",8888));
//        ctx.writeAndFlush(dp);
//        System.out.println("传输完毕");
        super.channelActive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, NioDatagramChannel nioDatagramChannel) throws Exception {

    }
}
