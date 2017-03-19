package com.itouch8.pump.core.util.scan.impl;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;

import com.itouch8.pump.core.util.CoreUtils;
import com.itouch8.pump.core.util.config.BaseConfig;
import com.itouch8.pump.core.util.exception.Throw;
import com.itouch8.pump.core.util.exception.meta.ExceptionCodes;
import com.itouch8.pump.core.util.filter.IFilter;
import com.itouch8.pump.core.util.filter.field.IFieldFilter;
import com.itouch8.pump.core.util.filter.method.IMethodFilter;
import com.itouch8.pump.core.util.filter.type.ITypeFilter;
import com.itouch8.pump.core.util.scan.IScan;


public class SimpleScan implements IScan {

    private static MetadataReaderFactory metadataReaderFactory = null;

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Class<?>> scanClasses(String basePackage, ITypeFilter filter) {
        return scan(basePackage, filter, new ScanCallback<Class<?>>() {
            @Override
            void callback(Set<Class<?>> set, IFilter<Class<?>> filter, ClassMetadata classMetadata, Class<?> cls) {
                if (null == filter || filter.accept(cls)) {
                    set.add(cls);
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Method> scanMethods(String basePackage, IMethodFilter filter) {
        return scan(basePackage, filter, new ScanCallback<Method>() {
            @Override
            void callback(Set<Method> set, IFilter<Method> filter, ClassMetadata classMetadata, Class<?> cls) {
                for (Class<?> c = cls; !Object.class.equals(c); c = c.getSuperclass()) {
                    Method[] methods = c.getDeclaredMethods();
                    if (null != methods) {
                        for (Method method : methods) {
                            if (!set.contains(method) && (null == filter || filter.accept(method))) {
                                set.add(method);
                            }
                        }
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Field> scanFields(String basePackage, IFieldFilter filter) {
        return scan(basePackage, filter, new ScanCallback<Field>() {
            @Override
            void callback(Set<Field> set, IFilter<Field> filter, ClassMetadata classMetadata, Class<?> cls) {
                for (Class<?> c = cls; !Object.class.equals(c); c = c.getSuperclass()) {
                    Field[] fields = c.getDeclaredFields();
                    if (null != fields) {
                        for (Field field : fields) {
                            if (!set.contains(field) && (null == filter || filter.accept(field))) {
                                set.add(field);
                            }
                        }
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<File> scanFiles(String basePackage, FileFilter filter) {
        if (CoreUtils.isBlank(basePackage)) {
            return Collections.<File>emptySet();
        } else {
            basePackage = basePackage.replace('.', '/');
        }
        Set<File> candidates = new LinkedHashSet<File>();
        try {
            for (String bp : basePackage.split(",")) {
                String packageSearchPath = "classpath*:" + bp + "/**/*";
                ResourcePatternResolver resolver = BaseConfig.getResourcePatternResolver();
                Resource[] resources = resolver.getResources(packageSearchPath);
                for (Resource resource : resources) {
                    if (resource.isReadable()) {
                        try {
                            File file = resource.getFile();
                            if (null != file && (null == filter || filter.accept(file))) {
                                candidates.add(file);
                            }
                        } catch (Throwable ignore) {
                        }
                    }
                }
            }
        } catch (IOException ex) {
            Throw.throwRuntimeException(ExceptionCodes.BF010103, ex, basePackage);
        }
        return candidates;
    }

    /**
     * 扫描满足条件的类，并调用回调函数
     * 
     * @param basePackage
     * @param filter
     * @param callback
     * @return
     */
    private <E> Set<E> scan(String basePackage, IFilter<E> filter, ScanCallback<E> callback) {
        if (CoreUtils.isBlank(basePackage)) {
            return Collections.<E>emptySet();
        } else {
            basePackage = basePackage.replace('.', '/');
        }
        Set<E> candidates = new LinkedHashSet<E>();
        try {
            for (String bp : basePackage.split(",")) {
                String packageSearchPath = "classpath*:" + bp + "/**/*.class";
                ResourcePatternResolver resolver = BaseConfig.getResourcePatternResolver();
                Resource[] resources = resolver.getResources(packageSearchPath);
                MetadataReaderFactory metadataReaderFactory = getMetadataReaderFactory(resolver);
                for (Resource resource : resources) {
                    if (resource.isReadable()) {
                        try {
                            MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
                            ClassMetadata meta = metadataReader.getClassMetadata();
                            if (meta.isAnnotation() || meta.isInterface() || meta.hasEnclosingClass() || meta.isAbstract()) {
                            } else {
                                String classname = meta.getClassName();
                                Class<?> cls = Class.forName(classname);
                                callback.callback(candidates, filter, meta, cls);
                            }
                        } catch (Throwable ignore) {
                        }
                    }
                }
            }
        } catch (IOException ex) {
            Throw.throwRuntimeException(ExceptionCodes.BF010103, ex, basePackage);
        }
        return candidates;
    }

    private MetadataReaderFactory getMetadataReaderFactory(ResourcePatternResolver resolver) {
        if (null == metadataReaderFactory) {
            synchronized (SimpleScan.class) {
                if (null == metadataReaderFactory) {
                    metadataReaderFactory = new CachingMetadataReaderFactory(resolver);
                }
            }
        }
        return metadataReaderFactory;
    }

    /**
     * 类扫描回调抽象类（内部类）
     * 
     * @param <E>
     */
    private abstract class ScanCallback<E> {
        abstract void callback(Set<E> set, IFilter<E> filter, ClassMetadata classMetadata, Class<?> cls);
    }
}
