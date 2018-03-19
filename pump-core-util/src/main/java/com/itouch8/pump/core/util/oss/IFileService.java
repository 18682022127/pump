package com.itouch8.pump.core.util.oss;

import java.io.InputStream;
import java.io.OutputStream;

public interface IFileService {

    public void send(String fileId, String base64);

    public void send(String fileId, InputStream s, String contentType);

    public String get(String id);

    /**
     * 获取文件后保存到输出流
     * 
     * @param fileId
     * @param os
     */
    public void get(String fileId, OutputStream os);

    public void del(String id);

}
