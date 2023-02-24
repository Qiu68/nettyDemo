package server;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * @version 1.0
 * @Author:qiu
 * @Description
 * @Date 16:08 2023/2/24
 **/
public class UdpTest {

   static DatagramSocket ds ;
    public static void main(String[] args) {

        Thread t1 = new Thread(()->{
            int count = 1;
            FileOutputStream out = null;
            try {
                out = new FileOutputStream("d:/doctor-2023.h264",true);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            //BufferedOutputStream bops = new BufferedOutputStream(out);
            byte[] data = new byte[48*1024];
            try {
                ds = new DatagramSocket(8888);
            } catch (SocketException e) {
                e.printStackTrace();
            }
            while(true) {

                DatagramPacket dp = new DatagramPacket(data, data.length);

                try {
                    try{
                        ds.receive(dp);
                    }
                    catch (SocketException e1){
                        System.out.println("没有连接啦");
                        out.flush();
                        return;
                    }
                } catch (IOException e) {
                    System.out.println("没有连接哦！");
                    e.printStackTrace();
                }
                //int length = trim(dp.getData());
                byte[] temp = new byte[dp.getLength()];
                System.arraycopy(dp.getData(),0,temp,0,dp.getLength());
                try {

                    out.write(temp);
                    out.flush();
                    System.out.println("接收的大小:"+temp.length+"循环次数:"+count++);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        });
        t1.start();
    }
}
