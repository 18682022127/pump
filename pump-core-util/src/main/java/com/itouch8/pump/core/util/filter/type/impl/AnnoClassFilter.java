package com.itouch8.pump.core.util.filter.type.impl;

import java.lang.annotation.Annotation;


public class AnnoClassFilter extends InstanceableClassFilter {

    
    private Class<? extends Annotation> anno;

    
    public AnnoClassFilter() {}

    
    public AnnoClassFilter(Class<? extends Annotation> anno) {
        this.anno = anno;
    }

    
    public boolean accept(Class<?> cls) {
        if (cls.isAnnotationPresent(getAnno()) && super.accept(cls)) {
            return true;
        }
        return false;
    }

    
    public Class<? extends Annotation> getAnno() {
        return anno;
    }

    
    public void setAnno(Class<? extends Annotation> anno) {
        this.anno = anno;
    }
}
