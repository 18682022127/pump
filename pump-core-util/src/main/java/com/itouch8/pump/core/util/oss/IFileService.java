package com.itouch8.pump.core.util.oss;

import java.io.InputStream;

public interface IFileService {

    public void send(String fileId, String base64);

    public void send(String fileId, InputStream s, String contentType);

    public String get(String id);

    public void del(String id);

}
