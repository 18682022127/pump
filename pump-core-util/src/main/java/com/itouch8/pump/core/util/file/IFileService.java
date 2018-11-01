package com.itouch8.pump.core.util.file;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import com.itouch8.pump.web.upload.IUploadFile;

/**
 * Copy Right Information :  <br>
 * Project :  <br>
 * Description : 文件服务接口<br>
 * Author : Huangzhong<br>
 * Version : 1.0.0 <br>
 * Since : 1.0.0 <br>
 * Date : 2018-10-30<br>
 */
public interface IFileService {

    /**
     * 上传二进制文件
     * 
     * @param fileId
     * @param is
     */
    public void send(String fileId, InputStream is, String contentType);

    /**
     * 批量上传二进制文件
     * 
     * @param fileId
     * @param is
     */
    public void send(String[] fileids, InputStream[] is, String[] contentType);

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
