package com.itouch8.pump.core.util.exception.meta;


public interface IExceptionMetaLoader {

    
    public IExceptionMeta lookup(String code, Throwable cause);
}
