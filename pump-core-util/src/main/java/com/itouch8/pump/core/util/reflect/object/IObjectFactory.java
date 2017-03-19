package com.itouch8.pump.core.util.reflect.object;

import java.util.List;


public interface IObjectFactory {

    
    <T> T create(Class<T> type, Object... constructorArgs);

    
    <T> T create(Class<T> type, List<Class<?>> constructorArgTypes, Object... constructorArgs);

    
    Object create(String className, Object... constructorArgs);

    
    Object create(String className, List<Class<?>> constructorArgTypes, Object... constructorArgs);
}
