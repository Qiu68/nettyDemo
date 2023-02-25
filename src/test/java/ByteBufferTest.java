import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class ByteBufferTest {
    public static void main(String[] args) throws IOException {

        ByteBuffer buf = ByteBuffer.allocate(64*1024);
        FileInputStream in = new FileInputStream("d:/doctor-2022.h264");
        FileOutputStream out = new FileOutputStream("d:/doctor2-2022.h264");
        //RandomAccessFile randomAccessFile;

        FileChannel inChannel = in.getChannel();
//        MappedByteBuffer map = inChannel.map(FileChannel.MapMode.READ_WRITE,
//                0, 1024);
        FileChannel outChannel = out.getChannel();

        boolean flag = true; //判断文件是否未结束
        int k = 1;
        long start = System.currentTimeMillis();
        while (true) {
            buf.clear();
            int read = inChannel.read(buf);
            if (read <= -1){
                break;
            }
            buf.flip();
            outChannel.write(buf);
        }
        System.out.println(System.currentTimeMillis() - start);
        System.out.println("文件读取结束");
        in.close();
        out.close();
        System.out.println("");
        System.out.println("");
        mmpTest();
    }

    static void mmpTest() throws IOException {
            RandomAccessFile inra = new RandomAccessFile("d:/doctor-2022.h264", "r");
            RandomAccessFile outra = new RandomAccessFile("d:/doctor3-2022.h264", "rw");
            FileChannel inraChannel = inra.getChannel();
            FileChannel outraChannel = outra.getChannel();

            long size = inraChannel.size();
            System.out.println("文件总大小:"+size);
            //定义文件需要使用的变量
            long count = 1;//复制文件的块数
            long startindex = 0;//每次复制文件的开始索引
            long everysize = 512 * 1024 * 1024;//每块大小 变成字节
            long copysize = size;//每次复制文件的大小，默认值等于文件的总大小
            if (size > everysize) {
                count = size % everysize == 0 ? size / everysize : size / everysize + 1;
                copysize = everysize;
            }
        long start = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
                MappedByteBuffer inmap = inraChannel.map(FileChannel.MapMode.READ_ONLY, startindex, copysize);
                MappedByteBuffer outmap = outraChannel.map(FileChannel.MapMode.READ_WRITE, startindex, copysize);
                System.out.println("每块文件大小：" + copysize);
                System.out.println("每块文件开始复制的索引：" + startindex);
                for (int j = 0; j < copysize; j++) {
                    byte b = inmap.get(j);
                    outmap.put(j, b);
                }
                startindex += copysize;
                copysize = size - startindex > everysize ? everysize : size - startindex;
            }
        System.out.println(System.currentTimeMillis()-start);
            inra.close();
            outra.close();
            inraChannel.close();
            outraChannel.close();
        }



}
