package com.itouch8.pump.core.util.aop;

import java.lang.reflect.Method;
import java.util.Map;


public interface IAopInterceptor {

    
    public boolean before(Map<String, Object> context, Object target, Method method, Object[] args);

    
    public boolean after(Map<String, Object> context, Object target, Method method, Object[] args, Object result);

    
    public boolean afterException(Map<String, Object> context, Object target, Method method, Object[] args, Throwable throwable);

    
    public boolean afterReturn(Map<String, Object> context, Object target, Method method, Object[] args);
}
