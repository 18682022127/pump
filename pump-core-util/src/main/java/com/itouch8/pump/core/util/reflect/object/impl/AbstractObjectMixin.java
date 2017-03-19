package com.itouch8.pump.core.util.reflect.object.impl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import com.itouch8.pump.core.util.CoreUtils;
import com.itouch8.pump.core.util.config.BaseConfig;
import com.itouch8.pump.core.util.reflect.object.IObjectFactory;
import com.itouch8.pump.core.util.reflect.object.IObjectMixin;


public abstract class AbstractObjectMixin<T> implements IObjectMixin<T> {

    
    private final Class<T> innerType;

    
    private IObjectFactory factory;

    
    private String className;

    
    private Class<? extends T> classType;

    public AbstractObjectMixin() {
        this(null, null);
    }

    public AbstractObjectMixin(String className) {
        this(className, null);
    }

    public AbstractObjectMixin(Class<T> classType) {
        this(null, classType);
    }

    public AbstractObjectMixin(String className, Class<T> classType) {
        this.innerType = this.getInnerType();
        this.className = className;
        this.classType = classType;
    }

    @Override
    public T getInstance(Object... constructorArgs) {
        IObjectFactory factory = getObjectFactoryInner();
        String className = getClassName();
        if (!CoreUtils.isBlank(className)) {
            Object rs = factory.create(className, constructorArgs);
            return CoreUtils.convert(rs, innerType);
        }

        Class<? extends T> cls = getClassType();
        if (null != cls) {
            return factory.create(cls, constructorArgs);
        }

        return factory.create(innerType, constructorArgs);
    }

    @Override
    public T getInstance(List<Class<?>> constructorArgTypes, Object... constructorArgs) {
        IObjectFactory factory = getObjectFactoryInner();
        String className = getClassName();
        if (!CoreUtils.isBlank(className)) {
            Object rs = factory.create(className, constructorArgTypes, constructorArgs);
            return CoreUtils.convert(rs, innerType);
        }

        Class<? extends T> cls = getClassType();
        if (null != cls) {
            return factory.create(cls, constructorArgTypes, constructorArgs);
        }

        return factory.create(innerType, constructorArgTypes, constructorArgs);
    }

    public IObjectFactory getFactory() {
        return factory;
    }

    public void setFactory(IObjectFactory factory) {
        this.factory = factory;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Class<? extends T> getClassType() {
        return classType;
    }

    public void setClassType(Class<? extends T> classType) {
        this.classType = classType;
    }

    @SuppressWarnings("unchecked")
    private Class<T> getInnerType() {
        Type t = getClass().getGenericSuperclass();
        ParameterizedType p = (ParameterizedType) t;
        return (Class<T>) p.getActualTypeArguments()[0];
    }

    private IObjectFactory getObjectFactoryInner() {
        IObjectFactory factory = getFactory();
        if (factory == null) {
            factory = BaseConfig.getObjectFactory();
        }
        return factory;
    }
}
