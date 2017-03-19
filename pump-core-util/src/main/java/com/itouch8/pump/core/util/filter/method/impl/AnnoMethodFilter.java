package com.itouch8.pump.core.util.filter.method.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import com.itouch8.pump.core.util.filter.method.IMethodFilter;


public class AnnoMethodFilter implements IMethodFilter {

    
    private Class<? extends Annotation> anno;

    
    public AnnoMethodFilter() {}

    
    public AnnoMethodFilter(Class<? extends Annotation> anno) {
        this.anno = anno;
    }

    
    public boolean accept(Method method) {
        if (null != method && method.isAnnotationPresent(getAnno())) {
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
