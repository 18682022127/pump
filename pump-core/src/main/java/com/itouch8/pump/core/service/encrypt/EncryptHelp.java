package com.itouch8.pump.core.service.encrypt;

import com.itouch8.pump.core.util.encrypt.impl.ConfigKeyEncrypt;


public class EncryptHelp {

    
    public static String encode(String value) {
        return new ConfigKeyEncrypt().encode(value, null);
    }
}
