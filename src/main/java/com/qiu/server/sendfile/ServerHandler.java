package com.qiu.server.sendfile;

import com.alibaba.fastjson.JSON;
import com.qiu.server.sendfile.pojo.FileBody;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;

import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

/**
 * @version 1.0
 * @Author:qiu
 * @Description
 * @Date 16:08 2023/2/27
 **/
public class ServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {
    static FileBody fileBody;
    static String clientIp = null;
    static Integer clientPort = null;
    static long start =0l;
    static long end = 0l;
    static int eventPageSize = 28*1024;
    static int pageCount = 1; //分段次数
    static FileInputStream fis;

    public ServerHandler(FileBody fileBody){
        this.fileBody = fileBody;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, DatagramPacket datagramPacket) throws Exception {
        /**
         * 1.取出客户端ip
         * 2.发送文件
         */

        //取客户端ip、端口
        InetSocketAddress sender = datagramPacket.sender();//取出客户端ip
        clientIp = sender.getHostString();
        clientPort = sender.getPort();
        if (clientIp == null || clientPort == null){
            throw new Exception("获取客户端ip出错！");
        }

        System.out.println("客户端:"+clientIp+":"+clientPort);

        //发送文件
        fis = new FileInputStream(fileBody.getFilePath()+fileBody.getFileName());
        FileChannel channel = fis.getChannel();
        fileBody.setAvailable(fis.available());//获取文件总大小

        pageCount = (int) (fileBody.getAvailable() % eventPageSize==0 //获取文件分段次数
                        ? fileBody.getAvailable() % eventPageSize
                        : fileBody.getAvailable() / eventPageSize +1l);


        fileBody.setEventPageSize(eventPageSize);


        for (int i=1;i<=pageCount;i++){
            ByteBuf buf = ByteBufAllocator.DEFAULT.buffer(1,68*1024);
            ByteBuffer buffer = ByteBuffer.allocate(28*1024);
            fileBody.setOrder(i);//设置数据包次序
            int readLength = channel.read(buffer);//读buffer.length个字节
            byte[] data = new byte[readLength];
            buffer.flip();
            buffer.get(data);
            fileBody.setData(data);
            System.out.println("数据大小:"+data.length);
            start = start + eventPageSize;
            fileBody.setStart(start);
            byte[] bytes = JSON.toJSONBytes(fileBody);//将对象序列化为字节数组
            String s = JSON.toJSONString(fileBody);
            System.out.println("分页次数:"+pageCount);
            System.out.println(s);
            System.out.println("对象大小："+bytes.length);
            buf.writeBytes(bytes);
            DatagramPacket packet = new DatagramPacket(buf, new InetSocketAddress(clientIp, clientPort));
            channelHandlerContext.writeAndFlush(packet);
            buffer.clear();


            System.out.println("分页次数:"+pageCount);
            Thread.sleep(2);

        }




    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("第一次连接");
        super.channelActive(ctx);
    }
}
