package com.qiu.s;


import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.channels.FileChannel;

/**
 * @version 1.0
 * @Author:qiu
 * @Description
 * @Date 10:59 2023/2/25
 **/
public class UdpReceiveServer {
    public static void main(String[] args) throws SocketException, FileNotFoundException {
        DatagramSocket ds = new DatagramSocket(8888);
        //设置udp接收数据的缓冲区大小，缓冲区太小会导致丢包
        ds.setReceiveBufferSize(1024*1024*10);
        FileOutputStream fos = new FileOutputStream("d:/2/default.h264");
        FileChannel fileChannel = fos.getChannel();
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        int count = 1;
        byte[] data = new byte[1400];
        DatagramPacket dp = new DatagramPacket(data,data.length);
        while (true){
            try {
                ds.receive(dp);
                System.out.println("接收的次数:"+count++ +"  接收的大小:"+dp.getLength());
                byte[] temp = new byte[dp.getLength()];
                System.arraycopy(dp.getData(),0,temp,0,dp.getLength());
                bos.write(temp);
                bos.flush();
            } catch (IOException e) {
                System.out.println("传输完成");
                break;
            }
        }
    }
}
