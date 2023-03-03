package ipc;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

/**
 * @version 1.0
 * @Author:qiu
 * @Description 不同程序间通信 B程序
 * @Date 12:23 2023/2/28
 **/
public class ApplicationB {
    // 1 读 2 写
    static int mode = 1;
    // 写文件的状态位 0 表示可以写入  1 表示其他应用程序在写文件
    static int status = 0;
    static RandomAccessFile file = null;
    public static void main(String[] args) throws IOException, InterruptedException {
        file = new RandomAccessFile("d:/data.txt","rw");
        FileChannel channel = file.getChannel();
        while (true){
            FileLock fileLock = channel.tryLock();
            if (fileLock != null){
                System.out.println("成功获取锁");
                file.write(123);
                file.write(456);
                System.out.println("B写入完成，释放锁");
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
        byte b = file.readByte();
        while (true){
            file.seek(0);
            b =  file.readByte();
            if (b == 0){
                //写入文件
                long length = file.length();
                file.seek(length);
                file.writeByte(6);
                System.out.println("写入成功");
                break;
            }
            else{
                System.out.println("当前文件在被其他程序修改");
                Thread.sleep(1000);
            }
        }
    }
}
