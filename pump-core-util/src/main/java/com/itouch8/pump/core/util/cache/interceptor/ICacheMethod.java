package com.itouch8.pump.core.util.cache.interceptor;

import java.lang.reflect.Method;
import java.util.Collection;

import org.springframework.cache.interceptor.CacheOperation;


public interface ICacheMethod {

    
    Method getMethod();

    
    Class<?> getTargetClass();

    
    Collection<CacheOperation> getOperations();
}
