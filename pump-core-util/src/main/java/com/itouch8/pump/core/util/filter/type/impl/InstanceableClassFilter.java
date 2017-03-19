package com.itouch8.pump.core.util.filter.type.impl;

import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.StandardClassMetadata;

import com.itouch8.pump.core.util.filter.type.ITypeFilter;


public class InstanceableClassFilter implements ITypeFilter {

    
    public boolean accept(Class<?> cls) {
        if (null == cls) {
            return false;
        }
        ClassMetadata meta = new StandardClassMetadata(cls);
        if (meta.isAbstract() || meta.isAnnotation() || meta.isInterface() && meta.hasEnclosingClass()) {
            return false;
        } else {
            return true;
        }
    }
}
