package cz;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * @version 1.0
 * @Author:qiu
 * @Description 文件索引 给定一个order id 返回文件内容的起始位置
 * @Date 15:16 2023/3/3
 **/
public class PacketIndex {

     String filePath;
     String fileName;
     int packetLength;
     long start;
     long end;
     long fileLength;
     RandomAccessFile raf = null;

    public PacketIndex(String filePath,String fileName,int packetLength) throws IOException {
        this.packetLength = packetLength;
        setFileLength(filePath,fileName);
    }

    private void setFileLength(String filePath,String fileName) throws IOException {
        this.filePath = filePath;
        this.fileName = fileName;
        raf = new RandomAccessFile(filePath+fileName,"r");
        fileLength = raf.length();
    }


    /**
     * 返回指定一个包 在文件中的起始读写位置
     * @param order
     * @return
     */
    public long[] getSeek(int order){
        long[] startWithEnd = new long[2];
        start =  order * packetLength;
        end = start + packetLength;
        startWithEnd[0] = start;
        startWithEnd[1] = end;
        return startWithEnd;
    }

}
