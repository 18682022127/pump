package com.itouch8.pump.core.service.mapping.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Set;

import com.itouch8.pump.core.util.CoreUtils;


public class AnnotationMethodMapping extends AbstractAnnotationMapping<Method> {

    @Override
    protected Set<Method> initializeMapping(String scanPackage, Class<? extends Annotation> cls) {
        return CoreUtils.scanMethods(scanPackage, cls);
    }

}
