package com.itouch8.pump.core.util.oss;

public interface IFileService {

    public void send(String fileId, String base64);

    public String get(String id);

    public void del(String id);

}
