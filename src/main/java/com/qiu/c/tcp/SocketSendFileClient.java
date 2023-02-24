package com.qiu.c.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.EmptyByteBuf;

import java.io.*;
import java.net.*;

/**
 * @version 1.0
 * @Author:qiu
 * @Description 原生Socket发送文件
 * @Date 11:28 2023/2/24
 **/
public class SocketSendFileClient {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1",9999);
        int count=1;
        int length;
        byte[] temp = new byte[48*1024];
        FileInputStream fis = new FileInputStream("d:/default.h264");
        BufferedInputStream bis = new BufferedInputStream(fis);
        BufferedOutputStream bos;

        try {
           bos = new BufferedOutputStream(socket.getOutputStream());

            while (true){
                length = bis.read(temp);
                if (length == -1){
                    System.out.println("文件已发送完毕");
                    break;
                }
                byte[] data = new byte[length];


                System.arraycopy(temp,0,data,0,length);
                bos.write(data);
                bos.flush();
                System.out.println("发送次数:"+count++ +"  发送字节数:"+length);
                Thread.sleep(10);
            }

            while (true){

            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}
