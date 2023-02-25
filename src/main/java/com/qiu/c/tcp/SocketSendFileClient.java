package com.qiu.c.tcp;



import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

/**
 * @version 1.0
 * @Author:qiu
 * @Description 原生Socket发送文件
 * @Date 11:28 2023/2/24
 **/
public class SocketSendFileClient {

    static void zeroCopy(String path,String fileName) throws IOException {
        long start = 0;
        long pageSize = 64*1024; //每段的字节数
        long eventPage = pageSize;
        long fileLength;
        long count = 1;
        FileInputStream fis = new FileInputStream(path+fileName);
        FileChannel fileChannel = fis.getChannel();
        SocketChannel socketChannel  = SocketChannel.open();
        socketChannel.configureBlocking(true);
        socketChannel.connect(new InetSocketAddress("127.0.0.1", 9999));


        String file = path + fileName;
        try {
            RandomAccessFile in = new RandomAccessFile(file,"r");
           fileLength = in.length();

           if (fileLength > eventPage) {
               eventPage = fileLength > eventPage ? eventPage : fileLength; //判断文件是否小于每次分段的大小
               count = fileLength % eventPage == 0 ? fileLength/eventPage : fileLength/eventPage+1;
           }

           for (int i=1;i<=count;i++){
               MappedByteBuffer out = fileChannel.map(FileChannel.MapMode.READ_ONLY, start, eventPage);
               socketChannel.write(out);
               start += eventPage;
               eventPage = fileLength - start > pageSize? pageSize:fileLength-start;
               System.out.println("发送次数:"+i + "  发送大小:"+eventPage);
           }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws IOException {

        zeroCopy("d:/","569mb.h264");
//        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 9999);
//        SocketChannel socketChannel = SocketChannel.open();
//        socketChannel.configureBlocking(true);
//        socketChannel.connect(address);
//        int count=1;
//        FileInputStream fis = new FileInputStream("d:/569mb.h264");
//        FileChannel fisChannel = fis.getChannel();
//
//        try {
//
//           ByteBuffer buffer = ByteBuffer.allocate(64*1024);
//
//
//            while (true){
//
//                int length = fisChannel.read(buffer);
//                buffer.flip();
//                if (length == -1){
//                    System.out.println("文件已发送完毕");
//                    break;
//                }
////                byte[] data = new byte[length];
//
////                System.arraycopy(temp,0,data,0,length);
////                bos.write(data);
////                bos.flush();
//                socketChannel.write(buffer);
//                System.out.println("发送次数:"+count++ +"  发送字节数:"+ length);
//                buffer.clear();
//                //Thread.sleep();
//
//            }
//
//            while (true){
//
//            }
//        } catch (SocketException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
////        } catch (InterruptedException e) {
////            e.printStackTrace();
//        }


    }
}
