package com.itouch8.pump.core.util.encrypt.impl;

import java.io.Serializable;

import com.itouch8.pump.core.util.config.BaseConfig;

public class ConfigKeyEncrypt extends BaseEncrypt implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -7641422443673764053L;

    @Override
    protected String getEncryptKey(String key) {
        return BaseConfig.getEncryptKey();
    }
}
