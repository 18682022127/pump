package com.itouch8.pump.util.json.serial;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ClassUtils;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.introspect.BasicBeanDescription;
import com.itouch8.pump.util.json.serial.converter.IJsonConverter;


public class SerialConfigContext {

    private static final ThreadLocal<Map<Class<?>, ISerialConfig>> context = new ThreadLocal<Map<Class<?>, ISerialConfig>>();

    private static final ThreadLocal<Map<JavaType, BasicBeanDescription>> bbdmap = new ThreadLocal<Map<JavaType, BasicBeanDescription>>();

    public static void addSerialConfigs(ISerialConfig... configs) {
        if (null != configs && 0 != configs.length) {
            Map<Class<?>, ISerialConfig> map = context.get();
            if (map == null) {
                map = new HashMap<Class<?>, ISerialConfig>();
                context.set(map);
            }
            for (ISerialConfig config : configs) {
                map.put(config.getSerialType(), config);
            }
        }
    }

    public static boolean isValid() {
        Map<Class<?>, ISerialConfig> map = context.get();
        if (map == null || map.isEmpty()) {
            return false;
        }
        for (ISerialConfig config : map.values()) {
            if (config.isValid()) {
                return true;
            }
        }
        return false;
    }

    public static void addBasicBeanDescription(JavaType type, BasicBeanDescription bbd) {
        Map<JavaType, BasicBeanDescription> map = bbdmap.get();
        if (map == null) {
            map = new HashMap<JavaType, BasicBeanDescription>();
            bbdmap.set(map);
        }
        map.put(type, bbd);
    }

    public static ISerialConfig getSerialConfig(Class<?> cls) {
        Map<Class<?>, ISerialConfig> map = context.get();
        if (null == map || map.isEmpty()) {
            return null;
        }
        for (Class<?> superclass = cls; superclass != null && !Object.class.equals(superclass);) {
            if (map.containsKey(superclass)) {
                return map.get(superclass);
            }
            superclass = superclass.getSuperclass();
        }
        List<Class<?>> interfaces = ClassUtils.getAllInterfaces(cls);
        if (null != interfaces && !interfaces.isEmpty()) {
            for (Class<?> c : interfaces) {
                if (map.containsKey(c)) {
                    return map.get(c);
                }
            }
        }
        return null;
    }

    public static BasicBeanDescription getBasicBeanDescription(JavaType type) {
        Map<JavaType, BasicBeanDescription> map = bbdmap.get();
        if (null == map || map.isEmpty()) {
            return null;
        }
        return map.get(type);
    }

    public static IJsonConverter getJsonConverter(Object bean, String name) {
        if (null != bean) {
            ISerialConfig config = SerialConfigContext.getSerialConfig(bean.getClass());
            if (null != config) {
                Map<String, IJsonConverter> jcs = config.getConverters();
                return null == jcs ? null : jcs.get(name);
            }
        }
        return null;
    }

    public static void remove() {
        context.remove();
        bbdmap.remove();
    }
}
