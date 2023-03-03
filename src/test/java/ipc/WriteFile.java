

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.charset.StandardCharsets;

/**
 * @version 1.0
 * @Author:qiu
 * @Description
 * @Date 10:54 2023/3/1
 **/
public class WriteFile {
    public static void main(String[] args) throws IOException, InterruptedException {
        test1();
    }

    static void test1() throws IOException, InterruptedException {
        String data = "111111111111111111111111111111111111111111111111111";
        FileOutputStream file = new FileOutputStream("d:/1.txt",true);
        FileLock fileLock = file.getChannel().tryLock();
        if (fileLock.isValid()){
            file.write(data.getBytes(StandardCharsets.UTF_8));
            Thread.sleep(2000);
        }
        if (fileLock.isValid()){
            fileLock.release();
        }
    }
}
