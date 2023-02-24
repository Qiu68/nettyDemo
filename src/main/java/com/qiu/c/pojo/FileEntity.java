package com.qiu.c.pojo;

import java.io.Serializable;
import java.util.Arrays;

public class FileEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private String fileName;

    private int start;

    private int end;

    private byte[] bytes;

    public FileEntity(String fileName, int start, int end, byte[] bytes) {
        this.fileName = fileName;
        this.start = start;
        this.end = end;
        this.bytes = bytes;
    }

    public FileEntity() {
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
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

    @Override
    public String toString() {
        return "FileEntity{" +
                "fileName='" + fileName + '\'' +
                ", start=" + start +
                ", end=" + end +
                '}';
    }
}
