package com.itouch8.pump.util.toolimpl;

import java.util.Map;

import com.itouch8.pump.core.util.CoreUtils;


public abstract class BeanUtilsImpl {

    private static final BeanUtilsImpl instance = new BeanUtilsImpl() {};

    private BeanUtilsImpl() {}

    
    public static BeanUtilsImpl getInstance() {
        return instance;
    }

    
    public Object getProperty(Object bean, String property) {
        return CoreUtils.getProperty(bean, property);
    }

    
    public <E> E getProperty(Object bean, String property, Class<E> resultType) {
        return CoreUtils.getProperty(bean, property, resultType);
    }

    
    public void setProperty(Object bean, String property, Object value) {
        CoreUtils.setProperty(bean, property, value);
    }

    
    public void removeProperty(Object bean, String property) {
        CoreUtils.removeProperty(bean, property);
    }

    
    public Object getProperty(Object bean, String expression, Map<String, Object> context) {
        return CoreUtils.getProperty(bean, expression, context);
    }

    
    public <E> E getProperty(Object bean, String expression, Map<String, Object> context, Class<E> resultType) {
        return CoreUtils.getProperty(bean, expression, context, resultType);
    }

    
    public void setProperty(Object bean, String expression, Object value, Map<String, Object> context) {
        CoreUtils.setProperty(bean, expression, value, context);
    }

    
    public Object[] getProperties(Object bean, String[] expressions) {
        return CoreUtils.getProperties(bean, expressions);
    }

    
    public Object[] getProperties(Object bean, String[] expressions, Map<String, Object> context) {
        return CoreUtils.getProperties(bean, expressions, context);
    }
}
