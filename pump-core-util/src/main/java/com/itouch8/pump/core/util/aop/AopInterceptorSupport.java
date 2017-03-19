package com.itouch8.pump.core.util.aop;

import java.lang.reflect.Method;
import java.util.Map;


public class AopInterceptorSupport implements IAopInterceptor {

    
    @Override
    public boolean before(Map<String, Object> context, Object target, Method method, Object[] args) {
        return true;
    }

    
    @Override
    public boolean after(Map<String, Object> context, Object target, Method method, Object[] args, Object result) {
        return true;
    }

    
    @Override
    public boolean afterException(Map<String, Object> context, Object target, Method method, Object[] args, Throwable throwable) {
        return true;
    }

    
    @Override
    public boolean afterReturn(Map<String, Object> context, Object target, Method method, Object[] args) {
        return true;
    }
}
