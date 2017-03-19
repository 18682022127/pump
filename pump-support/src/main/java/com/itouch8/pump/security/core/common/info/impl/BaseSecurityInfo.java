package com.itouch8.pump.security.core.common.info.impl;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.itouch8.pump.security.core.common.info.ISecurityInfo;


public class BaseSecurityInfo implements ISecurityInfo {

    
    private final Map<String, Object> map;

    
    private String code;

    
    private String message;

    
    private boolean success;

    public BaseSecurityInfo() {
        this(false);
    }

    public BaseSecurityInfo(boolean success) {
        this.success = success;
        this.map = new ConcurrentHashMap<String, Object>();
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public boolean isSuccess() {
        return success;
    }

    @Override
    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public void setProperty(String name, Object value) {
        this.map.put(name, value);
    }

    @Override
    public Object getProperty(String name) {
        return this.map.get(name);
    }

    @Override
    public <E> E getProperty(String name, Class<E> cls) {
        Object value = this.map.get(name);
        if (null != value) {
            if (cls.isAssignableFrom(value.getClass())) {
                return cls.cast(value);
            } else {
                throw new ClassCastException("cannot convert the type from [" + value.getClass().getName() + "] to [" + cls.getName() + "]");
            }
        }
        return null;
    }

    @Override
    public Iterator<String> getPeropertyNames() {
        return this.map.keySet().iterator();
    }
}
