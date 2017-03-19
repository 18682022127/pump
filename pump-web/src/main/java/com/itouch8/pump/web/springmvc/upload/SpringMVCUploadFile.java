package com.itouch8.pump.web.springmvc.upload;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

import com.itouch8.pump.core.util.exception.Throw;
import com.itouch8.pump.web.exception.WebExceptionCodes;
import com.itouch8.pump.web.upload.IUploadFile;


public class SpringMVCUploadFile implements IUploadFile {

    private MultipartFile delegete;

    public SpringMVCUploadFile(MultipartFile file) {
        this.delegete = file;
    }

    public String getName() {
        return delegete.getName();
    }

    public String getOriginalFilename() {
        return delegete.getOriginalFilename();
    }

    public String getContentType() {
        return delegete.getContentType();
    }

    public boolean isEmpty() {
        return delegete.isEmpty();
    }

    public long getSize() {
        return delegete.getSize();
    }

    public byte[] getBytes() {
        try {
            return delegete.getBytes();
        } catch (IOException e) {
            throw Throw.createRuntimeException(WebExceptionCodes.BF060001, e, getOriginalFilename());
        }
    }

    public InputStream getInputStream() {
        try {
            return delegete.getInputStream();
        } catch (IOException e) {
            throw Throw.createRuntimeException(WebExceptionCodes.BF060001, e, getOriginalFilename());
        }
    }

    public void transferTo(File file) {
        try {
            delegete.transferTo(file);
        } catch (IOException e) {
            throw Throw.createRuntimeException(WebExceptionCodes.BF060001, e, getOriginalFilename());
        }
    }

    public void deleteTmpFile() {

    }
}
