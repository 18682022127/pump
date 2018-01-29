package com.itouch8.pump.core.util.rsa.impl;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;

import com.itouch8.pump.core.util.rsa.AbstractKey;
import com.itouch8.pump.core.util.rsa.IKeyService;
import com.itouch8.pump.core.util.rsa.RSAUtils;

public abstract class AbstractRSAKeyService<T extends AbstractKey> implements IKeyService<T> {

    protected static String DEFAULT_CHAR_SET = "utf-8";

    @Override
    public abstract T initKey();

    @Override
    public abstract T getKey();

    public KeyProperty buildKey() {
        try {
            HashMap<String, Object> map = RSAUtils.getKeys();
            RSAPublicKey publicKey = (RSAPublicKey) map.get("public");
            RSAPrivateKey privateKey = (RSAPrivateKey) map.get("private");
            return new KeyProperty(publicKey.getModulus().toString(), publicKey.getPublicExponent().toString(), privateKey.getPrivateExponent().toString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String encrypt(String data) {
        try {
            KeyProperty kp = (KeyProperty) getKey();
            RSAPublicKey PublicKey = RSAUtils.getPublicKey(kp.getModule(), kp.getPublicEmpoent());
            return RSAUtils.encryptByPublicKey(data, PublicKey);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public String decrypt(String data) {

        try {
            KeyProperty kp = (KeyProperty) getKey();
            RSAPrivateKey privateKey = RSAUtils.getPrivateKey(kp.getModule(), kp.getPrivateEmpoent());
            return new StringBuffer(RSAUtils.decryptByPrivateKey(data, privateKey)).reverse().toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
