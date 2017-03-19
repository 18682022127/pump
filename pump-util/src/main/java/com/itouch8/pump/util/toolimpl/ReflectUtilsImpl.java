package com.itouch8.pump.util.toolimpl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;

import com.itouch8.pump.core.util.CoreUtils;
import com.itouch8.pump.core.util.config.BaseConfig;
import com.itouch8.pump.core.util.exception.Throw;
import com.itouch8.pump.core.util.filter.field.IFieldFilter;
import com.itouch8.pump.core.util.filter.field.impl.AnnoFieldFilter;
import com.itouch8.pump.core.util.filter.method.IMethodFilter;
import com.itouch8.pump.core.util.filter.type.ITypeFilter;


public abstract class ReflectUtilsImpl {

    private static final ReflectUtilsImpl instance = new ReflectUtilsImpl() {};

    private ReflectUtilsImpl() {}

    
    public static ReflectUtilsImpl getInstance() {
        return instance;
    }

    private static final Class<?>[] ORDERED_PRIMITIVE_TYPES = {Byte.TYPE, Short.TYPE, Character.TYPE, Integer.TYPE, Long.TYPE, Float.TYPE, Double.TYPE};

    
    public ClassLoader getDefaultClassLoader() {
        return CoreUtils.getDefaultClassLoader();
    }

    
    public Class<?> forName(String name) {
        return CoreUtils.forName(name);
    }

    
    public Class<?> forName(String name, ClassLoader classLoader) {
        return CoreUtils.forName(name, classLoader);
    }

    
    public List<Class<?>> convertClassNamesToClasses(List<String> classNames) {
        return CoreUtils.convertClassNamesToClasses(classNames);
    }

    
    public List<String> convertClassesToClassNames(List<Class<?>> classes) {
        return CoreUtils.convertClassesToClassNames(classes);
    }

    
    public List<String> convertClassesToClassNames(Class<?>[] classes) {
        if (classes == null) {
            return null;
        }
        List<String> classNames = new ArrayList<String>(classes.length);
        for (Class<?> cls : classes) {
            if (cls == null) {
                classNames.add(null);
            } else {
                classNames.add(cls.getName());
            }
        }
        return classNames;
    }

    
    public boolean isPresent(String className) {
        return isPresent(className, null);
    }

    
    public boolean isPresent(String className, ClassLoader classLoader) {
        return org.springframework.util.ClassUtils.isPresent(className, resolverClassLoader(classLoader));
    }

    
    public Class<?> getGenericType(Field field) {
        Class<?> cls = (Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
        return cls;
    }

    
    public <E extends Enum<E>> E getEnum(Class<E> enumClass, String enumName) {
        try {
            if (enumName == null) {
                return null;
            }
            return Enum.valueOf(enumClass, enumName);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    
    public <T> T clone(final T obj) {
        return ObjectUtils.clone(obj);
    }

    
    public Class<?> getUserClass(Object instance) {
        return getUserClass(instance.getClass());
    }

    
    public Class<?> getUserClass(Class<?> clazz) {
        return org.springframework.util.ClassUtils.getUserClass(clazz);
    }

    
    public List<Class<?>> getAllSuperclasses(Class<?> cls) {
        return ClassUtils.getAllSuperclasses(cls);
    }

    
    public List<Class<?>> getAllInterfaces(Class<?> cls) {
        return ClassUtils.getAllInterfaces(cls);
    }

    
    public boolean isSimpleCls(Class<?> cls) {
        if (null == cls) {
            return false;
        }
        return cls.equals(byte.class) || cls.equals(Byte.class) || cls.equals(char.class) || cls.equals(Character.class) || cls.equals(short.class) || cls.equals(Short.class) || cls.equals(int.class) || cls.equals(Integer.class) || cls.equals(long.class) || cls.equals(Long.class) || cls.equals(float.class) || cls.equals(Float.class) || cls.equals(double.class) || cls.equals(Double.class) || cls.equals(boolean.class) || cls.equals(Boolean.class)
        // || CharSequence.class.isAssignableFrom(type)//String.class StringBuilder.class
        // StringBuffer.class
                || cls.equals(String.class) || cls.equals(StringBuilder.class) || cls.equals(StringBuffer.class) || cls.equals(BigInteger.class) || cls.equals(BigDecimal.class) || cls.equals(Date.class);
    }

    public boolean isAssignable(Class<?> cls, Class<?> toClass) {
        return ClassUtils.isAssignable(cls, toClass);
    }

    public boolean isAssignable(Class<?>[] classArray, Class<?>... toClassArray) {
        return ClassUtils.isAssignable(classArray, toClassArray);
    }

    
    public boolean isInnerClass(Class<?> cls) {
        return cls != null && cls.getEnclosingClass() != null;
    }

    
    public <T> T newInstance(Class<T> cls) {
        return CoreUtils.newInstance(cls);
    }

    
    public Object newInstance(String className) {
        return CoreUtils.newInstance(className);
    }

    
    public <T> Constructor<T> getMatchingAccessibleConstructor(Class<T> cls, Class<?>... parameterTypes) {
        return ConstructorUtils.getMatchingAccessibleConstructor(cls, parameterTypes);
    }

    
    public void makeAccessible(Constructor<?> ctor) {
        if ((!Modifier.isPublic(ctor.getModifiers()) || !Modifier.isPublic(ctor.getDeclaringClass().getModifiers())) && !ctor.isAccessible()) {
            ctor.setAccessible(true);
        }
    }

    
    public <T> T invokeConstructor(Class<T> cls, Object... args) {
        try {
            return ConstructorUtils.invokeConstructor(cls, args);
        } catch (Exception e) {
            throw Throw.createRuntimeException(e);
        }
    }

    
    public <T> T invokeConstructor(Class<T> cls, Object[] args, Class<?>[] parameterTypes) {
        try {
            return ConstructorUtils.invokeConstructor(cls, args, parameterTypes);
        } catch (Exception e) {
            throw Throw.createRuntimeException(e);
        }
    }

    
    public boolean isPublicStaticFinalField(Field field) {
        int modifiers = field.getModifiers();
        return (Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers));
    }

    
    public Field getField(final Class<?> cls, String fieldName) {
        return FieldUtils.getField(cls, fieldName, true);
    }

    
    public void makeAccessible(Field field) {
        if ((!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers()) || Modifier.isFinal(field.getModifiers())) && !field.isAccessible()) {
            field.setAccessible(true);
        }
    }

    
    public Object readStaticField(Field field) {
        try {
            return FieldUtils.readStaticField(field, true);
        } catch (Exception e) {
            throw Throw.createRuntimeException(e);
        }
    }

    
    public Object readStaticField(Class<?> cls, String fieldName) {
        try {
            return FieldUtils.readStaticField(cls, fieldName, true);
        } catch (Exception e) {
            throw Throw.createRuntimeException(e);
        }
    }

    
    public Object readField(Field field, Object target) {
        try {
            return FieldUtils.readField(field, target, true);
        } catch (Exception e) {
            throw Throw.createRuntimeException(e);
        }
    }

    
    public Object readField(Object target, String fieldName) {
        try {
            return FieldUtils.readField(target, fieldName, true);
        } catch (Exception e) {
            throw Throw.createRuntimeException(e);
        }
    }

    
    public void writeStaticField(Field field, Object value) {
        try {
            FieldUtils.writeStaticField(field, value, true);
        } catch (Exception e) {
            throw Throw.createRuntimeException(e);
        }
    }

    
    public void writeStaticField(Class<?> cls, String fieldName, Object value) {
        try {
            FieldUtils.writeStaticField(cls, fieldName, value, true);
        } catch (Exception e) {
            throw Throw.createRuntimeException(e);
        }
    }

    
    public void writeField(Field field, Object target, Object value) {
        try {
            FieldUtils.writeField(field, target, value, true);
        } catch (Exception e) {
            throw Throw.createRuntimeException(e);
        }
    }

    
    public void writeField(Object target, String fieldName, Object value) {
        try {
            FieldUtils.writeField(target, fieldName, value, true);
        } catch (Exception e) {
            throw Throw.createRuntimeException(e);
        }
    }

    
    public Method getMatchingAccessibleMethod(Class<?> cls, String methodName, Class<?>... parameterTypes) {
        return MethodUtils.getMatchingAccessibleMethod(cls, methodName, parameterTypes);
    }

    
    public Class<?>[] getBestMatchClasses(Collection<Class<?>[]> clss, Class<?>[] cls) {
        if (null == clss || null == cls || clss.isEmpty()) {
            return null;
        } else {
            Set<Class<?>[]> matches = new HashSet<Class<?>[]>();
            for (Class<?>[] c : clss) {
                if (isAssignable(cls, c)) {
                    matches.add(c);
                }
            }
            if (matches.isEmpty()) {
                return null;
            } else if (matches.size() == 1) {
                return matches.iterator().next();
            } else {
                Iterator<Class<?>[]> i = matches.iterator();
                Class<?>[] m = i.next();
                for (; i.hasNext();) {
                    Class<?>[] c = i.next();
                    if (compareParameterTypes(c, m, cls) < 0) {
                        m = c;
                    }
                }
                return m;
            }
        }
    }

    
    public Class<?> getBestMatchClass(Collection<Class<?>> clss, Class<?> cls) {
        if (null == clss || null == cls || clss.isEmpty()) {
            return null;
        } else {
            Set<Class<?>> matches = new HashSet<Class<?>>();
            for (Class<?> c : clss) {
                if (isAssignable(cls, c)) {
                    matches.add(c);
                }
            }
            if (matches.isEmpty()) {
                return null;
            } else if (matches.size() == 1) {
                return matches.iterator().next();
            } else {
                Iterator<Class<?>> i = matches.iterator();
                Class<?> m = i.next();
                for (; i.hasNext();) {
                    Class<?> c = i.next();
                    if (compareParameterTypes(c, m, cls) < 0) {
                        m = c;
                    }
                }
                return m;
            }
        }
    }

    
    public void makeAccessible(Method method) {
        if ((!Modifier.isPublic(method.getModifiers()) || !Modifier.isPublic(method.getDeclaringClass().getModifiers())) && !method.isAccessible()) {
            method.setAccessible(true);
        }
    }

    
    public Object invokeMethod(Object target, String methodName, Object... args) {
        try {
            return MethodUtils.invokeMethod(target, methodName, args);
        } catch (Exception e) {
            throw Throw.createRuntimeException(e);
        }
    }

    
    public Object invokeMethod(Object target, String methodName, Object[] args, Class<?>[] parameterTypes) {
        try {
            return MethodUtils.invokeMethod(target, methodName, args, parameterTypes);
        } catch (Exception e) {
            throw Throw.createRuntimeException(e);
        }
    }

    
    public Object invokeStaticMethod(Class<?> cls, String methodName, Object[] args) {
        try {
            return MethodUtils.invokeStaticMethod(cls, methodName, args);
        } catch (Exception e) {
            throw Throw.createRuntimeException(e);
        }
    }

    
    public Object invokeStaticMethod(Class<?> cls, String methodName, Object[] args, Class<?>[] parameterTypes) {
        try {
            return MethodUtils.invokeStaticMethod(cls, methodName, args, parameterTypes);
        } catch (Exception e) {
            throw Throw.createRuntimeException(e);
        }
    }

    
    public <E> Set<Class<? extends E>> scanClassesByParentCls(String basePackage, Class<E> parent) {
        return CoreUtils.scanClassesByParentCls(basePackage, parent);
    }

    
    public <E extends Annotation> Set<Class<?>> scanClasses(String basePackage, Class<E> annoCls) {
        return CoreUtils.scanClasses(basePackage, annoCls);
    }

    
    public Set<Class<?>> scanClasses(String basePackage, ITypeFilter filter) {
        return CoreUtils.scanClasses(basePackage, filter);
    }

    
    public <E extends Annotation> Set<Method> scanMethods(String basePackage, Class<E> annoCls) {
        return CoreUtils.scanMethods(basePackage, annoCls);
    }

    
    public Set<Method> scanMethods(String basePackage, IMethodFilter filter) {
        return CoreUtils.scanMethods(basePackage, filter);
    }

    
    public <E extends Annotation> Set<Field> scanFields(String basePackage, Class<E> annoCls) {
        return scanFields(basePackage, new AnnoFieldFilter(annoCls));
    }

    
    public Set<Field> scanFields(String basePackage, IFieldFilter filter) {
        return BaseConfig.getScan().scanFields(basePackage, filter);
    }

    private ClassLoader resolverClassLoader(ClassLoader classLoader) {
        if (null == classLoader) {
            return this.getDefaultClassLoader();
        }
        return classLoader;
    }

    private int compareParameterTypes(final Class<?> left, final Class<?> right, final Class<?> actual) {
        final float leftCost = getObjectTransformationCost(actual, left);
        final float rightCost = getObjectTransformationCost(actual, right);
        return leftCost < rightCost ? -1 : rightCost < leftCost ? 1 : 0;
    }

    private int compareParameterTypes(final Class<?>[] left, final Class<?>[] right, final Class<?>[] actual) {
        final float leftCost = getTotalTransformationCost(actual, left);
        final float rightCost = getTotalTransformationCost(actual, right);
        return leftCost < rightCost ? -1 : rightCost < leftCost ? 1 : 0;
    }

    private float getTotalTransformationCost(final Class<?>[] srcArgs, final Class<?>[] destArgs) {
        float totalCost = 0.0f;
        for (int i = 0; i < srcArgs.length; i++) {
            Class<?> srcClass, destClass;
            srcClass = srcArgs[i];
            destClass = destArgs[i];
            totalCost += getObjectTransformationCost(srcClass, destClass);
        }
        return totalCost;
    }

    private float getObjectTransformationCost(Class<?> srcClass, final Class<?> destClass) {
        if (destClass.isPrimitive()) {
            return getPrimitivePromotionCost(srcClass, destClass);
        }
        float cost = 0.0f;
        while (srcClass != null && !destClass.equals(srcClass)) {
            if (destClass.isInterface() && ClassUtils.isAssignable(srcClass, destClass)) {
                cost += 0.25f;
                break;
            }
            cost++;
            srcClass = srcClass.getSuperclass();
        }
        if (srcClass == null) {
            cost += 1.5f;
        }
        return cost;
    }

    private float getPrimitivePromotionCost(final Class<?> srcClass, final Class<?> destClass) {
        float cost = 0.0f;
        Class<?> cls = srcClass;
        if (!cls.isPrimitive()) {
            cost += 0.1f;
            cls = ClassUtils.wrapperToPrimitive(cls);
        }
        for (int i = 0; cls != destClass && i < ORDERED_PRIMITIVE_TYPES.length; i++) {
            if (cls == ORDERED_PRIMITIVE_TYPES[i]) {
                cost += 0.1f;
                if (i < ORDERED_PRIMITIVE_TYPES.length - 1) {
                    cls = ORDERED_PRIMITIVE_TYPES[i + 1];
                }
            }
        }
        return cost;
    }
}
