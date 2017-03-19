package com.itouch8.pump.core.util.cache.interceptor.impl;

import java.lang.reflect.Method;
import java.util.Collection;

import org.springframework.cache.interceptor.CacheOperation;

import com.itouch8.pump.core.util.cache.interceptor.ICacheMethod;


public class CacheMethod implements ICacheMethod {

    private Method method;

    private Class<?> targetClass;

    private Collection<CacheOperation> operations;

    public CacheMethod(Method method, Class<?> targetClass, Collection<CacheOperation> operations) {
        this.method = method;
        this.targetClass = targetClass;
        this.operations = operations;
    }

    @Override
    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    @Override
    public Class<?> getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(Class<?> targetClass) {
        this.targetClass = targetClass;
    }

    @Override
    public Collection<CacheOperation> getOperations() {
        return operations;
    }

    public void setOperations(Collection<CacheOperation> operations) {
        this.operations = operations;
    }
}
