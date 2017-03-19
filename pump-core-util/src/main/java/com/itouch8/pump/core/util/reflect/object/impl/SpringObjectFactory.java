package com.itouch8.pump.core.util.reflect.object.impl;

import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


public class SpringObjectFactory extends ReflectObjectFactory implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    
    @Override
    protected Object doCreate(Class<?> type, Object[] args, Class<?>[] argTypes) throws Exception {
        try {
            if (null != applicationContext) {
                if (null == args || 0 == args.length) {
                    return applicationContext.getBean(type);
                } else {
                    return applicationContext.getBean(type, args);
                }
            }
        } catch (Exception e) {
            // ignore
        }
        return super.doCreate(type, args, argTypes);
    }

    
    @Override
    public Object create(String className, List<Class<?>> constructorArgTypes, Object... constructorArgs) {
        if (-1 == className.indexOf(".") && null != applicationContext) {
            if (null == constructorArgs || 0 == constructorArgs.length) {
                return applicationContext.getBean(className);
            } else {
                return applicationContext.getBean(className, constructorArgs);
            }
        }
        return super.create(className, constructorArgTypes, constructorArgs);
    }

    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
