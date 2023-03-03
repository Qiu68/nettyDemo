package com.qiu.c.udp;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.*;

/**
 * @version 1.0
 * @Author:qiu
 * @Description
 * @Date 14:46 2023/2/24
 **/
public class UdpTest {
    public static void main(String[] args) throws IOException {
        DatagramSocket ds = null;
        DatagramPacket dp = null;
        int count=1;
        int length;
        byte[] temp = new byte[64*1024];
        FileInputStream fis = new FileInputStream("d:/569mb.h264");
        BufferedInputStream bis = new BufferedInputStream(fis);

        try {
            ds = new DatagramSocket();

            while (true){
                length = bis.read(temp);
                if (length == -1){
                    System.out.println("文件已发送完毕");
                    break;
                }
                byte[] data = new byte[length];
                System.arraycopy(temp,0,data,0,length);
                dp = new DatagramPacket(data, data.length,
                        InetAddress.getByName("127.0.0.1"),8888);
                ds.send(dp);
                System.out.println("发送次数:"+count++ +"  发送字节数:"+length);
              Thread.sleep(2);
            }


        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}
