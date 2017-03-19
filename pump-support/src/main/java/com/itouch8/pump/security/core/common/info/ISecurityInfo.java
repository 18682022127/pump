package com.itouch8.pump.security.core.common.info;

import java.util.Iterator;


public interface ISecurityInfo {

    
    public String getCode();

    
    public void setCode(String code);

    
    public String getMessage();

    
    public void setMessage(String message);

    
    public boolean isSuccess();

    
    public void setSuccess(boolean success);

    
    public void setProperty(String name, Object value);

    
    public Object getProperty(String name);

    
    public <E> E getProperty(String name, Class<E> cls);

    
    public Iterator<String> getPeropertyNames();
}
