package com.itouch8.pump.core.util.reflect.method;


public interface IParamExtractor {

    
    public <E> E extract(Class<E> type, String expression, Object args);
}
