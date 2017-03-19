package com.itouch8.pump.core.util.config;

import java.util.Arrays;
import java.util.List;

import com.itouch8.pump.core.util.CoreUtils;


public class ConfigHelper {

    
    public static final int EQUAL_EMPTY_INT_VALUE = Integer.MIN_VALUE;

    
    public static int getValue(int value, String defaultConfigName) {
        if (EQUAL_EMPTY_INT_VALUE == value) {
            return Defaults.getDefaultConfig(defaultConfigName, int.class);
        }
        return value;
    }

    
    public static String getValue(String value, String defaultConfigName) {
        if (CoreUtils.isBlank(value)) {
            return Defaults.getDefaultConfig(defaultConfigName);
        }
        return value;
    }

    
    public static List<String> getValues(List<String> values, String defaultConfigName) {
        if (null == values || values.isEmpty()) {
            String[] s = Defaults.getDefaultConfigs(defaultConfigName);
            if (null != s && s.length >= 1) {
                return Arrays.asList(s);
            }
        }
        return values;
    }

    
    public static List<String> combineValues(List<String> values, String defaultConfigName) {
        String[] s = Defaults.getDefaultConfigs(defaultConfigName);
        if (null != s && s.length >= 1) {
            if (null == values || values.isEmpty()) {
                return Arrays.asList(s);
            } else {
                values.addAll(Arrays.asList(s));
            }
        }
        return values;
    }

    
    public static <E> E getComponent(E component, Class<E> cls) {
        if (null == component) {
            return Defaults.getDefaultComponent(cls);
        }
        return component;
    }

    
    public static <E> List<E> getComponents(List<E> components, Class<E> cls) {
        if (null == components || components.isEmpty()) {
            E[] d = Defaults.getDefaultComponents(cls);
            if (null != d && d.length > 0) {
                return Arrays.asList(d);
            }
        }
        return components;
    }

    
    public static <E> List<E> combineComponents(List<E> components, Class<E> cls) {
        E[] d = Defaults.getDefaultComponents(cls);
        if (null != d && d.length > 0) {
            if (null == components || components.isEmpty()) {
                return Arrays.asList(d);
            } else {
                components.addAll(Arrays.asList(d));
            }
        }
        return components;
    }

    
    public static <E> E getComponent(E component, Class<E> cls, String name) {
        if (null == component) {
            return Defaults.getDefaultComponent(cls, name);
        }
        return component;
    }

    
    public static <E> List<E> getComponents(List<E> components, Class<E> cls, String name) {
        if (null == components || components.isEmpty()) {
            E[] d = Defaults.getDefaultComponents(cls, name);
            if (null != d && d.length > 0) {
                return Arrays.asList(d);
            }
        }
        return components;
    }

    
    public static <E> List<E> combineComponents(List<E> components, Class<E> cls, String name) {
        E[] d = Defaults.getDefaultComponents(cls, name);
        if (null != d && d.length > 0) {
            if (null == components || components.isEmpty()) {
                return Arrays.asList(d);
            } else {
                components.addAll(Arrays.asList(d));
            }
        }
        return components;
    }
}
