package com.itouch8.pump.web.upload;

import java.io.InputStream;

public class FileData {

    private String fileName;

    private String fileType;

    private String fileBase64;

    private String fileThumbBase64;

    private String contentType;

    private long size;

    public byte[] bytes;

    public InputStream inputStream;

    private String fileAttrDess;

    private String fileAttrId;

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getFileBase64() {
        return fileBase64;
    }

    public void setFileBase64(String fileBase64) {
        this.fileBase64 = fileBase64;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public String getFileThumbBase64() {
        return fileThumbBase64;
    }

    public void setFileThumbBase64(String fileThumbBase64) {
        this.fileThumbBase64 = fileThumbBase64;
    }

    public String getFileAttrDess() {
        return fileAttrDess;
    }

    public void setFileAttrDess(String fileAttrDess) {
        this.fileAttrDess = fileAttrDess;
    }

    public String getFileAttrId() {
        return fileAttrId;
    }

    public void setFileAttrId(String fileAttrId) {
        this.fileAttrId = fileAttrId;
    }

}
