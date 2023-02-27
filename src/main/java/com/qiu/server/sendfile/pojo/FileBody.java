package com.qiu.server.sendfile.pojo;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @version 1.0
 * @Author:qiu
 * @Description 描述文件实体类
 * @Date 15:54 2023/2/27
 **/
public class FileBody implements Serializable {
    private String fileName;//文件名
    private String filePath;//文件路径
    private long available;//文件总大小
    private long start;//每次读的起始位置
    private int eventPageSize;//分段的大小
    private int order;//发送的顺序
    private byte[] data;


    public FileBody(String fileName, String filePath) {
        this.fileName = fileName;
        this.filePath = filePath;
    }

    public FileBody() {
    }


    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getAvailable() {
        return available;
    }

    public void setAvailable(long available) {
        this.available = available;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public int getEventPageSize() {
        return eventPageSize;
    }

    public void setEventPageSize(int eventPageSize) {
        this.eventPageSize = eventPageSize;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "FileBody{" +
                "fileName='" + fileName + '\'' +
                ", filePath='" + filePath + '\'' +
                ", available=" + available +
                ", start=" + start +
                ", eventPageSize=" + eventPageSize +
                ", order=" + order +'}';
    }
}
