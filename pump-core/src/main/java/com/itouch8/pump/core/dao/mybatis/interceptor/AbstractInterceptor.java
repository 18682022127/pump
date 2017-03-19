package com.itouch8.pump.core.dao.mybatis.interceptor;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.Properties;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;


public abstract class AbstractInterceptor implements Interceptor {

    private Properties properties;

    private static Field target;
    static {
        try {
            target = Plugin.class.getDeclaredField("target");
            target.setAccessible(true);
        } catch (Exception e) {
        }
    }

    
    protected <T> T getTarget(Invocation invocation, Class<T> cls) {
        Object obj = invocation.getTarget();
        while (Proxy.isProxyClass(obj.getClass())) {// 处理嵌套插件的目标类获取
            try {
                obj = target.get(Proxy.getInvocationHandler(obj));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return cls.cast(obj);
    }

    
    protected <T> T getArgument(Invocation invocation, Class<T> cls, int index) {
        Object obj = invocation.getArgs()[index];
        return cls.cast(obj);
    }

    
    protected String getProperty(String key) {
        return null == properties ? null : properties.getProperty(key);
    }

    
    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    
    @Override
    public void setProperties(Properties properties) {
        this.properties = properties;
    }

}
