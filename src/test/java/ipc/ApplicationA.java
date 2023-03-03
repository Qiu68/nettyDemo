package ipc;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

/**
 * @version 1.0
 * @Author:qiu
 * @Description 不同程序间通信
 * @Date 12:23 2023/2/28
 **/
public class ApplicationA {
    // 1 读 2 写
    static int mode = 1;
    // 写文件的状态位 0 表示可以写入  1 表示其他应用程序在写文件
    static int status = 0;
    static RandomAccessFile file = null;
    public static void main(String[] args) throws IOException, InterruptedException {
       file = new RandomAccessFile("d:/data.txt","rw");
       file.write(1);

    }

    static void fileLockTest() throws IOException, InterruptedException {
        file = new RandomAccessFile("d:/data.txt","rw");
        FileChannel channel = file.getChannel();
        while (true){
            FileLock fileLock = channel.tryLock();
            if (fileLock != null){
                System.out.println("成功获取锁！");
                file.write(123);
                file.write(456);
                System.out.println("A写入完成，释放锁");
                fileLock.release();
                break;
            }
            else{
                System.out.println("没有获取到锁！");
                Thread.sleep(1000);
            }
        }
    }
    static void test() throws IOException, InterruptedException {
        file = new RandomAccessFile("d:/data.txt","rw");
        file.seek(0);
        status = 1;
        file.write(status);
        for (int i=1;i<10;i++){
            file.write(i);
            //Thread.sleep(1000);
        }
        status = 0;
        file.seek(0);
        file.write(status);
        Thread.sleep(3000);
        file.seek(file.length()-1);
        System.out.println(file.readByte());
        file.close();
    }
}
