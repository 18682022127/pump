package com.itouch8.pump.core.util.reflect.method.impl;

import com.itouch8.pump.core.util.CoreUtils;
import com.itouch8.pump.core.util.reflect.method.IParamExtractor;


public abstract class AbstractParamExtractor implements IParamExtractor {

    
    @Override
    public <E> E extract(Class<E> type, String expression, Object args) {
        Object param = this.doExtract(type, expression, args);
        if (null != param) {
            if (type.isAssignableFrom(param.getClass())) {
                return type.cast(param);
            } else {
                return CoreUtils.convert(param, type);
            }
        }
        return null;
    }

    
    abstract protected Object doExtract(Class<?> type, String expression, Object args);
}
