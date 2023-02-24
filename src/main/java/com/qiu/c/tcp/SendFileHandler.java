package com.qiu.c.tcp;

import com.qiu.c.pojo.FileEntity;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.*;


public class SendFileHandler extends SimpleChannelInboundHandler {

    private FileEntity file;
//    private int start = 0;
//    private int end = 0;
    private int length = 0;
    private int flag = 1;
    private int sizeLength = 48*1024;
    private byte[] temp = new byte[sizeLength];
    private static BufferedInputStream in = null;
    private static FileInputStream fis = null;
    private static int count = 1;

    public SendFileHandler(FileEntity file){
        this.file = file;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        System.out.println("客户端 - 文件发送完毕");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("第一次发送");

        /**
         * 1.打开文件
         * 2.发送data个字节数
         */

        fis = new FileInputStream(file.getFileName());
        in = new BufferedInputStream(fis);
        length = in.read(temp);
        byte[] data = new byte[length];
        System.arraycopy(temp,0,data,0,length);
        file.setBytes(data);
        ctx.writeAndFlush(file);

        //使用RandomAccess不支持超过2G的大文件
//        randomAccess = new RandomAccessFile(file.getFileName(),"r");
//        start = file.getStart();
//        randomAccess.seek(start);
//        if (-1 != (length = randomAccess.read(temp))) {
//            byte[] data = new byte[length];
//            System.arraycopy(temp,0,data,0,length);
//            file.setEnd(length);
//            file.setBytes(temp);
//            ctx.writeAndFlush(file);
//        }
        super.channelActive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
        /**
         * 1.接收服务器发送已读字节数 start
         * 2.将文件指针移动到start
         * 3.判断剩余容量是否够发一次包
         * 4.判断文件内容是否发完
         */
        if (!(msg instanceof Integer)){
            throw new Exception();
        }
        System.out.println(msg);
        flag = (Integer) msg;
        if (flag != -1){
            if (in == null){
                throw new Exception("in对象为空");
            }
           length =  in.read(temp);
            if (length != -1) {
                byte[] data = new byte[length];
                System.arraycopy(temp, 0, data, 0, length);
                file.setBytes(data);
                System.out.println("发送次数:"+count++ +"发送大小:"+data.length);
                channelHandlerContext.writeAndFlush(file);
            }
            else {
                System.out.println("文件已传输完毕...");
                channelHandlerContext.close();
                in.close();
                fis.close();
            }

        }
        else {
            System.out.println("文件已传输完毕...");
            channelHandlerContext.close();
            in.close();
            fis.close();
        }
//        int readSize = 0;
//        if (start != -1){
//            randomAccess = new RandomAccessFile(file.getFileName(),"r");
//            randomAccess.seek(start);
//            int size = (int) (randomAccess.length() - start);
////            if (size < temp.length){
////               sizeLength = size;
////            }
//            byte[] data = new byte[sizeLength];
//           if ( -1 != (readSize= randomAccess.read(data))
//                   && randomAccess.length() - start > 0){
//               byte[] temp = new byte[readSize];
//               System.arraycopy(data,0,temp,0,readSize);
//               //将每次读取的字节数写入
//               file.setEnd(readSize);
//               file.setBytes(temp);
//               channelHandlerContext.writeAndFlush(file);
//           }
//           else {
//               System.out.println("文件发送完毕");
//               randomAccess.close();
//               channelHandlerContext.close();
//           }
//        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable e) {
        e.printStackTrace();
        ctx.close();
    }
}
