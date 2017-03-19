package com.itouch8.pump.core.util.encrypt.impl;

import com.itouch8.pump.core.util.config.BaseConfig;


public class ConfigKeyEncrypt extends BaseEncrypt {

    
    @Override
    protected String getEncryptKey(String key) {
        return BaseConfig.getEncryptKey();
    }
}
