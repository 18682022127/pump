package com.itouch8.pump.util.json.serial.converter.impl;

import com.itouch8.pump.util.Tool;
import com.itouch8.pump.util.json.serial.converter.IJsonConverter;


public class I18nJsonConverter implements IJsonConverter {

    
    @Override
    public Object convert(Object container, Object property, Object value) {
        String key = getLocaleKey(container, property, value);
        if (null == key) {
            return null;
        } else {
            return Tool.LOCALE.getMessage(key);
        }
    }

    
    protected String getLocaleKey(Object container, Object property, Object value) {
        if (value == null) {
            return null;
        } else {
            return value.toString();
        }
    }
}
