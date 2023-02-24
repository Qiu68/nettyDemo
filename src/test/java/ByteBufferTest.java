import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class ByteBufferTest {
    public static void main(String[] args) throws IOException {

        ByteBuffer buf = ByteBuffer.allocate(1);
        FileInputStream in = new FileInputStream("d:/1.txt");
        FileOutputStream out = new FileOutputStream("d:/2.txt");
        RandomAccessFile randomAccessFile;

        FileChannel inChannel = in.getChannel();
        FileChannel outChannel = out.getChannel();

        boolean flag = true; //判断文件是否未结束
        int k = 1;
        while (true) {
            buf.clear();
            int read = inChannel.read(buf);
            if (read <= -1){
                break;
            }
            buf.flip();
            outChannel.write(buf);
        }
        System.out.println("文件读取结束");
        in.close();
        out.close();
    }
}
