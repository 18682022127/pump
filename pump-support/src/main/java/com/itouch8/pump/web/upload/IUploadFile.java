package com.itouch8.pump.web.upload;

import java.io.File;
import java.io.InputStream;

/**
 * Copy Right Information :  <br>
 * Project :  <br>
 * Description : 文件上传对象<br>
 * Author : Huangzhong<br>
 * Version : 1.0.0 <br>
 * Since : 1.0.0 <br>
 * Date : 2018-10-30<br>
 */
public interface IUploadFile {

    public String getName();

    public String getOriginalFilename();

    public String getContentType();

    public boolean isEmpty();

    public long getSize();

    public byte[] getBytes();

    public InputStream getInputStream();

    public void transferTo(File file);

    public void deleteTmpFile();
}
