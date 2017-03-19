package com.itouch8.pump.core.util.reflect.object;

import java.util.List;


public interface IObjectMixin<T> {

    
    public T getInstance(Object... constructorArgs);

    
    public T getInstance(List<Class<?>> constructorArgTypes, Object... constructorArgs);
}
