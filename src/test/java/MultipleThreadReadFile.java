import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * @version 1.0
 * @Author:qiu
 * @Description 多线程读取文件
 * @Date 14:27 2023/2/28
 **/
public class MultipleThreadReadFile {
    public static void main(String[] args) throws IOException {

//        singThread();
        multipleThread();
    }

    static void singThread() throws IOException {
        RandomAccessFile file = new RandomAccessFile("d:/doctor-2022.h264","rw");
        byte[] data = new byte[1024*1024];
        long start = System.currentTimeMillis();
        long readSize = file.read(data);
        while (readSize != -1){
            readSize = file.read(data);
        }
        System.out.println(System.currentTimeMillis() - start);
    }

    static void multipleThread() throws IOException {
        long eventSize = 1024*1024;
        RandomAccessFile file = new RandomAccessFile("d:/doctor-2022.h264","rw");
        long t1_length;
        long t2_length;
        if (file.length()%eventSize == 0){
            t1_length = t2_length = file.length() / 2;
        }
        else {
            t1_length = file.length() / 2 + file.length() % eventSize;
            t2_length = file.length() / 2;
        }
        file.close();
        Thread t1 = new Thread(()->{
            byte[] temp = new byte[1024*1024];
            try {
                RandomAccessFile t1_file = new RandomAccessFile("d:/doctor-2022.h264",
                        "rw");
                long readLength = 0;
                long start = System.currentTimeMillis();
                while (readLength < t1_length){
                    t1_file.read(temp);
                    readLength = readLength + temp.length;
//                    byte[] data = new byte[temp.length];
//                    System.arraycopy(temp,0,data,0,temp.length);
                }
                System.out.println(System.currentTimeMillis() - start);
                System.out.println("t1读完");

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        Thread t2 = new Thread(()->{
            byte[] temp = new byte[1024*1024];
            try {
                RandomAccessFile t2_file = new RandomAccessFile("d:/doctor-2022.h264",
                        "rw");
                t2_file.seek(t1_length + 1);
                long readLength = 0;
                long start = System.currentTimeMillis();
                while (readLength < t2_length){
                    t2_file.read(temp);
                    readLength = readLength + temp.length;
//                    byte[] data = new byte[temp.length];
//                    System.arraycopy(temp,0,data,0,temp.length);
                }
                System.out.println(System.currentTimeMillis() - start);
                System.out.println("t2读完");

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        t1.start();
        t2.start();
    }

}
