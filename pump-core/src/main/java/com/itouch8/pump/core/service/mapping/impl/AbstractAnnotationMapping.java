package com.itouch8.pump.core.service.mapping.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Set;

import com.itouch8.pump.core.util.CoreUtils;
import com.itouch8.pump.core.util.exception.Throw;


public abstract class AbstractAnnotationMapping<V extends AnnotatedElement> extends AbstractMapping<String, V> {

    
    private String scanPackage;

    
    private Class<? extends Annotation> annoCls;

    
    private String idProperty = "value";

    
    private Method idMethod;

    
    private String orderProperty;

    
    private Method orderMethod;

    @Override
    protected Set<V> initializeMapping() {
        return initializeMapping(getScanPackage(), getAnnoCls());
    }

    @Override
    protected String getKey(V value) {
        try {
            Annotation anno = value.getAnnotation(annoCls);
            return (String) idMethod.invoke(anno, new Object[0]);
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    protected int getOrder(V value) {
        if (null != orderMethod) {
            try {
                Annotation anno = value.getAnnotation(annoCls);
                return (int) orderMethod.invoke(anno, new Object[0]);
            } catch (Exception e) {
            }
        }
        return 0;
    }

    @Override
    protected void init() {
        Class<? extends Annotation> annoCls = getAnnoCls();
        String idProperty = getIdProperty();
        Method idMethod = getMethod(annoCls, idProperty);
        if (null == idMethod) {
            idMethod = getMethod(annoCls, "value");
        }
        if (null == idMethod) {
            idMethod = getMethod(annoCls, "id");
        }
        if (null == idMethod) {
        	Throw.throwRuntimeException("注解类" + annoCls + "中找不到属性" + idProperty);
        } else {
            this.idMethod = idMethod;
        }
        String orderProperty = getOrderProperty();
        if (!CoreUtils.isBlank(orderProperty)) {
            this.orderMethod = getMethod(annoCls, orderProperty);
        }
        super.init();
    }

    
    protected abstract Set<V> initializeMapping(String scanPackage, Class<? extends Annotation> cls);

    private Method getMethod(Class<?> cls, String field) {
        try {
            return cls.getDeclaredMethod(field, new Class<?>[0]);
        } catch (Exception e) {
            return null;
        }
    }

    public String getScanPackage() {
        return scanPackage;
    }

    public void setScanPackage(String scanPackage) {
        this.scanPackage = scanPackage;
    }

    public Class<? extends Annotation> getAnnoCls() {
        return annoCls;
    }

    public void setAnnoCls(Class<? extends Annotation> annoCls) {
        this.annoCls = annoCls;
    }

    public String getIdProperty() {
        return idProperty;
    }

    public void setIdProperty(String idProperty) {
        this.idProperty = idProperty;
    }

    public String getOrderProperty() {
        return orderProperty;
    }

    public void setOrderProperty(String orderProperty) {
        this.orderProperty = orderProperty;
    }
}
