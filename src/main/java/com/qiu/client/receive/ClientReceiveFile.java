package com.qiu.client.receive;

import com.alibaba.fastjson.JSON;
import com.qiu.server.sendfile.pojo.FileBody;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

/**
 * @version 1.0
 * @Author:qiu
 * @Description
 * @Date 16:23 2023/2/27
 **/
public class ClientReceiveFile extends SimpleChannelInboundHandler<DatagramPacket> {

    static FileOutputStream out;



    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        /**
         * 1.给服务打招呼
         * 2.接收服务器数据
         */

        //1.打招呼
        System.out.println("连接建立");
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer(1,6);
        byteBuf.writeBytes("你好".getBytes(StandardCharsets.UTF_8));
        DatagramPacket datagramPacket = new DatagramPacket(byteBuf,
                new InetSocketAddress("127.0.0.1",9999));
        out = new FileOutputStream("d:/2/2.txt");
        ctx.writeAndFlush(datagramPacket);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, DatagramPacket datagramPacket) throws Exception {
        //2.接收服务器数据
        FileChannel channel = out.getChannel();

        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer(1,100*1024);
        buf = datagramPacket.content();
        byte[] data = new byte[buf.readableBytes()];
        buf.readBytes(data);
       FileBody file = (FileBody)JSON.parseObject(data, FileBody.class);
       //System.out.println("服务器响应:"+new String(data));
        System.out.println("服务器:"+datagramPacket.sender()+"  接收的字节数:"+data.length);
        System.out.println("文件对象:"+file.toString());
        byte[] bytes = new byte[file.getData().length];
        bytes = file.getData();
        System.out.println("数据大小:"+bytes.length);
        ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
        buffer.put(bytes);
        buffer.flip();
        channel.write(buffer);
        buffer.clear();
    }
}
