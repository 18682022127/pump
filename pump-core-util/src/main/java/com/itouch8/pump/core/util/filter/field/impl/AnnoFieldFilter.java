package com.itouch8.pump.core.util.filter.field.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import com.itouch8.pump.core.util.filter.field.IFieldFilter;


public class AnnoFieldFilter implements IFieldFilter {

    
    private Class<? extends Annotation> anno;

    
    public AnnoFieldFilter() {}

    
    public AnnoFieldFilter(Class<? extends Annotation> anno) {
        this.anno = anno;
    }

    
    public boolean accept(Field field) {
        if (null != field && field.isAnnotationPresent(getAnno())) {
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
