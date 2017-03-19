package com.itouch8.pump.util.toolimpl;

import java.util.List;

import com.itouch8.pump.core.util.CoreUtils;
import com.itouch8.pump.core.util.config.BaseConfig;


public abstract class ConvertUtilsImpl {

    private static final ConvertUtilsImpl instance = new ConvertUtilsImpl() {};

    private ConvertUtilsImpl() {}

    
    public static ConvertUtilsImpl getInstance() {
        return instance;
    }

    
    public boolean string2Boolean(String str) {
        return CoreUtils.string2Boolean(str);
    }

    
    public <T> T cast(Object obj) {
        return CoreUtils.cast(obj);
    }

    
    public <T> T convert(Object source, Class<T> targetType) {
        return BaseConfig.getConversionService().convert(source, targetType);
    }

    
    public <E> List<E> convertToList(Object source, Class<E> elementType) {
        return CoreUtils.convertToList(source, elementType);
    }

    
    public <T extends Number> T convertNumberToTargetClass(Number number, Class<T> targetClass) {
        return CoreUtils.convertNumberToTargetClass(number, targetClass);
    }

    
    public <T extends Number> T convertStringToTargetClass(String text, Class<T> targetClass) {
        return CoreUtils.convertStringToTargetClass(text, targetClass);
    }

    
    public String convertIp(String ip) {
        return CoreUtils.convertIp(ip);
    }
}
