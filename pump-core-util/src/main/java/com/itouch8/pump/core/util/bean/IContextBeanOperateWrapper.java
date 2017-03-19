package com.itouch8.pump.core.util.bean;

import java.util.Map;


public interface IContextBeanOperateWrapper extends IBeanOperateWrapper {

    
    public Object getProperty(Object bean, String expression, Map<String, Object> context);

    
    public <E> E getProperty(Object bean, String expression, Map<String, Object> context, Class<E> resultType);

    
    public void setProperty(Object bean, String expression, Object value, Map<String, Object> context);
}
