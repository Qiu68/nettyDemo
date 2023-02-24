package com.qiu.server;


import com.qiu.c.pojo.FileEntity;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.File;
import java.io.RandomAccessFile;

/**
 * 自定义处理器
 */
public class FileNettyServerHandler extends SimpleChannelInboundHandler {

    private int readLength;
    private static long start = 1;
    //private String receivePath = "d:/2/";
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof FileEntity){
            FileEntity sendFile = (FileEntity) msg;
            byte[] bytes = sendFile.getBytes();
            readLength = sendFile.getEnd();
           //String fileName = sendFile.getFileName();
            String path = "d:/2/doctor-2022.h264";
            File file = new File(path);

            /**
             * RandomAccessFile是Java 输入/输出流体系中功能最丰富的文件内容访问类，它提供了众多的方法来访问文件内容，
             * 它既可以读取文件内容，也可以向文件输出数据。与普通的输入/输出流不同的是，
             * RandomAccessFile支持"随机访问"的方式，程序可以直接跳转到文件的任意地方来读写数据。
             */
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
            System.out.println(sendFile.toString());
            randomAccessFile.seek(start);
            randomAccessFile.write(bytes);
            start = start + readLength;
            System.out.println("返回字节数:"+start);
            if(start > 0){
                ctx.writeAndFlush(start);
                randomAccessFile.close();
            }else {
                ctx.flush();
                ctx.close();
            }
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception{
        super.channelInactive(ctx);
        ctx.flush();
        ctx.close();
    }
}

