package com.qiu.s.udp;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;


import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;



/**
 * @version 1.0
 * @Author:qiu
 * @Description
 * @Date 14:50 2023/2/24
 **/                                     //这里的DatagramPacket要选Netty的,选Java.net Read方法就不会执行
public class ReceiveUdpHandler extends SimpleChannelInboundHandler<DatagramPacket> {


    private static FileOutputStream fos;
    private static BufferedOutputStream bos;
    private static int count = 1;
    private static String path = "d:/2/";
    private static String filename = "569mb.h264";

    static {
        try {
            fos = new FileOutputStream(path + filename);
            bos = new BufferedOutputStream(fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("初次连接");
        //ctx.channel().read();
        super.channelActive(ctx);
    }



    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //System.out.println(ctx.toString());
        super.channelReadComplete(ctx);
    }


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, DatagramPacket datagramPacket) throws Exception {
        System.out.println(datagramPacket.toString());

        DatagramPacket dp = datagramPacket;

        int length = datagramPacket.content().readableBytes();

        int flag = length;
        if (length > 0) {
            byte[] data = new byte[length];
            datagramPacket.content().readBytes(data);
            //System.arraycopy(data, 0, data, 0, length);
            System.out.println("接收次数:" + count++ + "   接收大小:" + data.length);
            bos.write(data);
            bos.flush();
            channelHandlerContext.writeAndFlush(flag);
        } else {
            flag = -1;
            channelHandlerContext.writeAndFlush(flag);
            bos.close();
            fos.close();
            channelHandlerContext.close();
        }
    }
}
