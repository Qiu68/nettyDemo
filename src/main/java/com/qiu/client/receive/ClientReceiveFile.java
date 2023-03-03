package com.qiu.client.receive;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.TimeoutException;

/**
 * @version 1.0
 * @Author:qiu
 * @Description
 * @Date 9:51 2023/2/28
 **/
public class ClientReceiveFile {

    static int ack = 0;
    static Queue<Integer> lossPacket = new ArrayDeque<>();

    public static void main(String[] args) {

        Thread t1 = new Thread(()->{
            while (true) {
                if (lossPacket.isEmpty()) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("lossPacket:" + lossPacket.poll());
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        t1.start();
        try {
            int count = 1;
            DatagramSocket datagramSocket = new DatagramSocket();
            byte[] msg = "hello server".getBytes(StandardCharsets.UTF_8);
            byte[] receiveMsg = new byte[40*1024+4];

            DatagramPacket sendDatagramPacket = new DatagramPacket(msg,
                    msg.length,new InetSocketAddress("127.0.0.1",8888));
            datagramSocket.send(sendDatagramPacket);

            DatagramPacket receiveDatagramPacket = new DatagramPacket(receiveMsg,
                    receiveMsg.length);

            datagramSocket.receive(receiveDatagramPacket);
            byte[] data1 = new byte[receiveDatagramPacket.getData().length];
            System.arraycopy(receiveDatagramPacket.getData(),
                    0,data1,0,data1.length);
            System.out.println("最后一个包的大小："+data1.length + "内容："+Util.bytesToInt(data1));

            datagramSocket.setReceiveBufferSize(1024*1024*100);
            FileOutputStream fos = new FileOutputStream("d:/2/287m.h264");
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            datagramSocket.setSoTimeout(60000);
            while (true){
                try{
                    datagramSocket.receive(receiveDatagramPacket);
                    int readLength = receiveDatagramPacket.getLength();

                    byte[] orderByte =new  byte[4];
                    //获取数据包的顺序id
                    System.arraycopy(receiveDatagramPacket.getData(),0,orderByte,0,4);
                    //将顺序id转换成int
                    int order = Util.bytesToInt(orderByte);
                    //将顺序id转换成int
                    System.out.println("order:"+ order + "  接收数据:"+readLength + "  " +
                            "接收到的次数:"+count++);
                    //进行ack确认

                    if (order - ack == 1){
                        byte[] data = new byte[readLength-4];
                        //去掉顺序id的四个字节，然后将数据包内容写入文件
                        System.arraycopy(receiveDatagramPacket.getData(),4
                                ,data,0,readLength-4);

                        bos.write(data);
                        bos.flush();
                        ack = order;
                    }

                    else {
                        //丢包了，将没有接收到的数据包id发送给服务端
                        for (int i = ack+1;i<order;i++){
                            lossPacket.add(i);
                        }
                        ack = order;
                    }
                }
                catch (SocketTimeoutException e){
                    System.out.println("超时");
                }




            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
