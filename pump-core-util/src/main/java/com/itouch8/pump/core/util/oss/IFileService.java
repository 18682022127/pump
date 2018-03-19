package com.itouch8.pump.core.util.oss;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import com.itouch8.pump.web.upload.IUploadFile;

public interface IFileService {

    /**
     * 上传base64格式的文件
     * 
     * @param fileId
     * @param base64
     */
    @Deprecated
    public void send(String fileId, String base64);

    /**
     * 上传二进制文件
     * 
     * @param fileId
     * @param is
     */
    public void send(String fileId, InputStream is, String contentType);

    /**
     * 上传文件
     * 
     * @param fileId
     * @param is
     */
    public Map<String, String> send(IUploadFile[] files);

    /**
     * 按字符串获取文件内容(主要用户获取文本格式存储的文件，如按base64格式存储的图片文件)
     * 
     * @param fileId
     * @return
     */
    public String get(String fileId);

    /**
     * 获取文件后保存到输出流
     * 
     * @param fileId
     * @param os
     */
    public void get(String fileId, OutputStream os);

    /**
     * 删除文件
     * 
     * @param fileId
     */
    public void del(String fileId);

}
