package com.itouch8.pump.core.util.scan;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;

import com.itouch8.pump.core.util.filter.field.IFieldFilter;
import com.itouch8.pump.core.util.filter.method.IMethodFilter;
import com.itouch8.pump.core.util.filter.type.ITypeFilter;


public interface IScan {

    
    public Set<Class<?>> scanClasses(String basePackage, ITypeFilter filter);

    
    public Set<Method> scanMethods(String basePackage, IMethodFilter filter);

    
    public Set<Field> scanFields(String basePackage, IFieldFilter filter);

    
    public Set<File> scanFiles(String basePackage, FileFilter filter);
}
