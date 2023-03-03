package ipc;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

/**
 * @version 1.0
 * @Author:qiu
 * @Description
 * @Date 17:47 2023/2/28
 **/
public class ReadFile {

    static  ServerSocket socket;
    static Socket accept;
    static RandomAccessFile raf;
//    static {
//        try {
//            socket = new ServerSocket(10086);
//            accept = socket.accept();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public static void main(String[] args) throws IOException, InterruptedException {
        raf = new RandomAccessFile("D:/1.txt", "rw");
        byte[] bytes = new byte[1024];
        FileLock flock=null;
        long start = 0l;
        long end = raf.length();
        System.out.println(end);
        for(int i=0;i<100;i++){
            raf = new RandomAccessFile("D:/1.txt", "rw");
            FileChannel fc = raf.getChannel();
            //移动到上次文件末尾
            raf.seek(end);
            flock=fc.lock();    //上锁
            if (flock.isValid()) {
                int read = raf.read(bytes);
                //System.out.println(read);
                if (read != -1) {
                    byte[] data = new byte[read];
                    System.arraycopy(bytes, 0, data, 0, read);
                    System.out.println(new String(data));
                    flock.release();    //释放锁
                    end = raf.length();
                    Thread.sleep(1000);
                    raf.close();
                    fc.close();
                }
                if (flock.isValid()) {
                    flock.release();
                    Thread.sleep(1000);
                }
                continue;
            }
            else {
                Thread.sleep(1);
            }
        }
    }

    }

//    /**
//     * 文件内容比较法
//     * @throws IOException
//     * @throws Exception
//     */
//    static void test1() throws IOException, InterruptedException {
//
//    }


