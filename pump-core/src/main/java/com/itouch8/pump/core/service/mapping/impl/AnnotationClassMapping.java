package com.itouch8.pump.core.service.mapping.impl;

import java.lang.annotation.Annotation;
import java.util.Set;

import com.itouch8.pump.core.util.CoreUtils;


public class AnnotationClassMapping extends AbstractAnnotationMapping<Class<?>> {

    @Override
    protected Set<Class<?>> initializeMapping(String scanPackage, Class<? extends Annotation> cls) {
        return CoreUtils.scanClasses(scanPackage, cls);
    }

}
