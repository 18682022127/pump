package com.itouch8.pump.web.upload;

import java.io.File;
import java.io.InputStream;

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
