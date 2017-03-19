package com.itouch8.pump.core.util.reflect.object.impl;

import org.apache.commons.lang3.reflect.ConstructorUtils;


public class ReflectObjectFactory extends AbstractObjectFactory {

    
    @Override
    protected Object doCreate(Class<?> type, Object[] args, Class<?>[] argTypes) throws Exception {
        return ConstructorUtils.invokeConstructor(type, args, argTypes);
    }
}
