package com.qiu.s.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class ReceiveFileTcpHandler extends SimpleChannelInboundHandler {

    private static int start = 0;
    private static FileOutputStream fos;
    private static BufferedOutputStream bos;
    private static int count = 1;
    private static String path = "d:/2/";
    private static String filename = "default.h264";

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
        super.channelActive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        /**
         * 1.获取file对象
         * 2.获取file的bytes属性
         * 3.获取已经读取的字节数
         * 4.将接受到的字节数写入文件
         * 5.更新start 更新为目前已经写入的字节数
         */

//        if (!(o instanceof FileEntity)){
//            throw new Exception();
//        }

       // FileEntity  file = (FileEntity) o;
//        byte[] bytes = (byte[]) o;
        System.out.println(o.toString());
        ByteBuf buf = (ByteBuf) o;
        byte[] bytes = new byte[buf.readableBytes()];
        System.out.println("buf.readableBytes:"+buf.readableBytes());
         buf.readBytes(bytes);
        int length = bytes.length;
        int flag = length;
        if (length > 0){
            byte[] data = new byte[length];
            System.arraycopy(bytes,0,data,0,length);
            System.out.println("接收次数:"+count++ +"接收大小:"+data.length);
            bos.write(data);
            bos.flush();
            channelHandlerContext.writeAndFlush(flag);
        }
//        RandomAccessFile randomAccessFile = new RandomAccessFile("d:/2/doctor-2022.h264","rw");
//        randomAccessFile.seek(start);
//        randomAccessFile.write(bytes);
//        //将每次接受到的字节数加上起始字节数，得到下一次写入的位置
//        start = start + length;
//        if (length > 0){
//            channelHandlerContext.writeAndFlush(start);
//            randomAccessFile.close();
//        }
        else {
            flag = -1;
            channelHandlerContext.writeAndFlush(flag);
            bos.close();
            fos.close();
            channelHandlerContext.close();

        }

    }
}
