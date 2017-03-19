package com.itouch8.pump.core.util.bean;


public interface IBeanOperateWrapper {

    
    public Object getProperty(Object bean, String property);

    
    public <E> E getProperty(Object bean, String property, Class<E> resultType);

    
    public void setProperty(Object bean, String property, Object value);

    
    public void removeProperty(Object bean, String property);
}
