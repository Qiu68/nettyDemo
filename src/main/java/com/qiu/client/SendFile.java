package com.qiu.client;



import java.io.File;
import java.io.Serializable;


public class SendFile implements Serializable {

    private static final long serrialVersionUID = 1L;

    private File file;
    private String fileName;
    private long start;
    private int end;
    private byte[] bytes;

    public SendFile(File file, String fileName, long start, int end, byte[] bytes) {
        this.file = file;
        this.fileName = fileName;
        this.start = start;
        this.end = end;
        this.bytes = bytes;
    }

    public SendFile() {

    }


    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
}
