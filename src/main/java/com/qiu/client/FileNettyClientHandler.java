package com.qiu.client;


import com.qiu.c.pojo.FileEntity;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * 自定义处理器
 */
public class FileNettyClientHandler extends SimpleChannelInboundHandler {

    private int readLength = 1;
    private long start = 0;
    private static int lastLength = 1;
    private static long size = 1l;
    private RandomAccessFile randomAccessFile;
    private FileEntity sendFile;
    public FileNettyClientHandler(FileEntity sendFile){
        this.sendFile = sendFile;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        System.out.println("客户端 - 文件发送完毕");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        try {
            randomAccessFile = new RandomAccessFile(sendFile.getFileName(),"r");
            randomAccessFile.seek(sendFile.getStart());
            lastLength = 48 * 1024;
            byte[] bytes = new byte[lastLength];
            if((readLength = randomAccessFile.read(bytes)) != -1){
                byte[] temp = new byte[readLength];
                System.arraycopy(bytes,0,temp,0,readLength);
                sendFile.setEnd(readLength);
                sendFile.setBytes(temp);
                // writeAndFlush: 写队列并刷新
                ctx.writeAndFlush(sendFile);
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof Long){
            //获取服务器以读取的数据
            start = (Long) msg;
            System.out.println("服务器返回的start字节数:"+start);
            lastLength = 48*1024;
            byte[] bytes = new byte[lastLength];

            if(start != -1){
                randomAccessFile = new RandomAccessFile(sendFile.getFileName(),"r");
                //移动到还没有读取的数据
                randomAccessFile.seek(start);
                //获取文件剩余长度
                 size = (randomAccessFile.length() - start);
                System.out.println("文件剩余大小:"+randomAccessFile.length());
                //System.out.println("length:"+length + "  lastLength:"+length);
//                if(size < lastLength){
//                    //如果剩余长度不足1024*1024 就以剩余长度发一个包
//                    lastLength = length;
//                }
                //如果文件没有读完，就继续读
                if((readLength = randomAccessFile.read(bytes)) != -1 && (randomAccessFile.length() - start)>0){
                    byte[] data = new byte[readLength];
                    System.arraycopy(bytes,0,data,0,readLength);
                    sendFile.setEnd(readLength);
                    sendFile.setBytes(data);
                    ctx.writeAndFlush(sendFile);
                }else {
                    randomAccessFile.close();
                    ctx.close();
                    System.out.println("本地文件准备完毕");
                }
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable e) {
        e.printStackTrace();
        ctx.close();
    }
}
