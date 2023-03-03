package com.qiu.server.sendfile;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.InetSocketAddress;


/**
 * @version 1.0
 * @Author:qiu
 * @Description 服务器向客户端传输文件处理器
 * @Date 16:08 2023/2/27
 **/
public class ServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {
    static String clientIp = null;
    static Integer clientPort = null;
    static FileInputStream fis;
    static BufferedInputStream bis;
    static int readLength = 63*1024;
    static byte[] bytes = new byte[readLength];
    static int readCount = 0; //读文件的次数
    static {
        try {
            fis = new FileInputStream("d:/569mb.h264");
            bis = new BufferedInputStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
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

        //读文件的次数
        long fileSize = fis.getChannel().size();
        System.out.println(fis.available());
        readCount = (int) (fileSize % readLength == 0
                        ? fileSize / readLength
                        : fileSize / readLength + 1);

        System.out.println("readcount:"+readCount);
        //发送文件
        for (int i =1;i<=readCount;i++){
            ByteBuf buf = ByteBufAllocator.DEFAULT.buffer(1,64*1024);
            int length = bis.read(bytes);
            byte[] data = new byte[length];
            System.arraycopy(bytes,0,data,0,length);
            buf.writeBytes(data);
            DatagramPacket datagramPackets = new DatagramPacket(buf,
                    new InetSocketAddress(clientIp,clientPort));
            channelHandlerContext.writeAndFlush(datagramPackets);
            System.out.println("发送次数:"+ i + "  发送字节数:"+data.length);
            //Thread.sleep(1);

        }



    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("第一次连接");
        super.channelActive(ctx);
    }

    void test(){

        /**
         * 将文件内容封装为对象，序列化后发送
         */
//       long start =0l;
//       long end = 0l;
//       int eventPageSize = 28*1024;
//       int pageCount = 1; //分段次数
//        fis = new FileInputStream(fileBody.getFilePath()+fileBody.getFileName());
//        FileChannel channel = fis.getChannel();
//        fileBody.setAvailable(fis.available());//获取文件总大小
//
//        pageCount = (int) (fileBody.getAvailable() % eventPageSize==0 //获取文件分段次数
//                ? fileBody.getAvailable() % eventPageSize
//                : fileBody.getAvailable() / eventPageSize +1l);
//
//
//        fileBody.setEventPageSize(eventPageSize);
//
//
//        for (int i=1;i<=pageCount;i++){
//            ByteBuf buf = ByteBufAllocator.DEFAULT.buffer(1,68*1024);
//            ByteBuffer buffer = ByteBuffer.allocate(28*1024);
//            fileBody.setOrder(i);//设置数据包次序
//            int readLength = channel.read(buffer);//读buffer.length个字节
//            byte[] data = new byte[readLength];
//            buffer.flip();
//            buffer.get(data);
//            fileBody.setData(data);
//            System.out.println("数据大小:"+data.length);
//            start = start + eventPageSize;
//            fileBody.setStart(start);
//            byte[] bytes = JSON.toJSONBytes(fileBody);//将对象序列化为字节数组
//            String s = JSON.toJSONString(fileBody);
//            System.out.println("分页次数:"+pageCount);
//            System.out.println(s);
//            System.out.println("对象大小："+bytes.length);
//            buf.writeBytes(bytes);
//            DatagramPacket packet = new DatagramPacket(buf, new InetSocketAddress(clientIp, clientPort));
//            channelHandlerContext.writeAndFlush(packet);
//            buffer.clear();
//
//
//            System.out.println("分页次数:"+pageCount);
////            Thread.sleep(2);
//
//        }
    }
}
