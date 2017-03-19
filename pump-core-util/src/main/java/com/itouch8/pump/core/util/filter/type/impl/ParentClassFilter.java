package com.itouch8.pump.core.util.filter.type.impl;


public class ParentClassFilter extends InstanceableClassFilter {

    
    private Class<?> parent;

    
    public ParentClassFilter() {}

    
    public ParentClassFilter(Class<?> parent) {
        this.parent = parent;
    }

    
    public boolean accept(Class<?> cls) {
        if (null != parent && parent.isAssignableFrom(cls) && super.accept(cls)) {
            return true;
        }
        return false;
    }

    
    public Class<?> getParent() {
        return parent;
    }

    
    public void setParent(Class<?> parent) {
        this.parent = parent;
    }
}
