package com.itouch8.pump.core.util.encrypt.impl;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import com.itouch8.pump.core.util.encrypt.IEncrypt;


public class BaseEncrypt implements IEncrypt {

    private String algorithm = "AES";
    private String encoding = "UTF-8";

    
    @Override
    public String encode(String data, String key) {
        try {
            key = getEncryptKey(key);
            
            SecretKey secretKey = new SecretKeySpec(key.getBytes(encoding), algorithm);
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return new String(Base64.encodeBase64(cipher.doFinal(data.getBytes(encoding))));
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

    
    @Override
    public String decode(String data, String key) {
        try {
            key = getEncryptKey(key);
            
            SecretKey secretKey = new SecretKeySpec(key.getBytes(encoding), algorithm);
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.decodeBase64(data)), encoding);
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

    
    protected String getEncryptKey(String key) {
        return key;
    }

    
    public String getAlgorithm() {
        return algorithm;
    }

    
    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    
    public String getEncoding() {
        return encoding;
    }

    
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }
}
