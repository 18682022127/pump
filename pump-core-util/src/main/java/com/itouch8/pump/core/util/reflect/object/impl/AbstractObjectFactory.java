package com.itouch8.pump.core.util.reflect.object.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.itouch8.pump.core.util.CoreUtils;
import com.itouch8.pump.core.util.reflect.object.IObjectFactory;


public abstract class AbstractObjectFactory implements IObjectFactory {

    
    @Override
    public Object create(String className, Object... constructorArgs) {
        return this.create(className, null, constructorArgs);
    }

    
    @Override
    public Object create(String className, List<Class<?>> constructorArgTypes, Object... constructorArgs) {
        Class<?> type = resolveInterface(className);
        return this.create(type, constructorArgTypes, constructorArgs);
    }

    
    @Override
    public <T> T create(Class<T> type, Object... constructorArgs) {
        return this.create(type, null, constructorArgs);
    }

    
    @Override
    public <T> T create(Class<T> type, List<Class<?>> constructorArgTypes, Object... constructorArgs) {
        try {
            Class<?> classToCreate = resolveInterface(type);
            Class<?>[] types = null;
            if (null != constructorArgTypes) {
                int size = constructorArgTypes.size();
                types = new Class<?>[size];
                for (int i = 0; i < size; i++) {
                    types[i] = constructorArgTypes.get(i);
                }
            }
            Object rs = this.doCreate(classToCreate, constructorArgs, types);
            if (null == rs) {
                return null;
            } else if (type.isInstance(rs)) {
                return type.cast(rs);
            } else {
                return CoreUtils.convert(rs, type);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error instantiating " + type + ". Cause: " + e, e);
        }
    }

    
    abstract protected Object doCreate(Class<?> type, Object[] args, Class<?>[] argTypes) throws Exception;

    
    protected Class<?> resolveInterface(String className) {
        try {
            return Class.forName(className);
        } catch (Exception e) {
            throw new RuntimeException("Error loading class " + className + ". Cause: " + e, e);
        }
    }

    
    protected Class<?> resolveInterface(Class<?> type) {
        Class<?> classToCreate;
        if (type == List.class || type == Collection.class || type == Iterable.class) {
            classToCreate = ArrayList.class;
        } else if (type == Map.class) {
            classToCreate = HashMap.class;
        } else if (type == SortedSet.class) {
            classToCreate = TreeSet.class;
        } else if (type == Set.class) {
            classToCreate = HashSet.class;
        } else {
            classToCreate = type;
        }
        return classToCreate;
    }
}
