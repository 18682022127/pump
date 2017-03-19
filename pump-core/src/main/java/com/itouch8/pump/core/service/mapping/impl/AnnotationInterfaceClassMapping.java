package com.itouch8.pump.core.service.mapping.impl;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;


public class AnnotationInterfaceClassMapping<E> extends AnnotationClassMapping {

    private Class<?> parentCls;

    @Override
    protected Set<Class<?>> initializeMapping(String scanPackage, Class<? extends Annotation> cls) {
        Set<Class<?>> set = super.initializeMapping(scanPackage, cls);
        if (null != set && null != getParentCls()) {
            Class<?> parentCls = getParentCls();
            Set<Class<?>> rs = new HashSet<Class<?>>();
            for (Class<?> c : set) {
                if (parentCls.isAssignableFrom(c)) {
                    rs.add(c);
                }
            }
            return rs;
        }
        return set;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<? extends E> get(String key) {
        Class<? extends E> cls = (Class<? extends E>) super.get(key);
        return cls;
    }

    public Class<?> getParentCls() {
        return parentCls;
    }

    public void setParentCls(Class<?> parentCls) {
        this.parentCls = parentCls;
    }
}
