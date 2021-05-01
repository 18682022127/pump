package com.itouch8.pump.core.util.config;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.springframework.cglib.core.ReflectUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.itouch8.pump.core.util.CoreUtils;
import com.itouch8.pump.core.util.logger.CommonLogger;

public class Defaults {

    private static final Properties properties = new Properties();

    private static final Map<String, Object> map = new HashMap<String, Object>();

    static {
        loadDefaults();
        loadCombineDefaults();
    }

    /**
     * 获取默认设置
     * 
     * @param name 配置名称
     * @return 字符串类型的设置值
     */
    public static String getDefaultConfig(String name) {
        return getDefaultConfig(name, String.class);
    }

    /**
     * 获取默认设置组
     * 
     * @param name 配置名称
     * @return 字符串数组类型的设置值，使用逗号分隔配置多个值
     */
    public static String[] getDefaultConfigs(String name) {
        return getDefaultConfigs(name, String.class);
    }

    /**
     * 获取默认设置
     * 
     * @param name 配置名称
     * @param cls 目标类型，一般为八种基本类型和字符串
     * @return 目标类型的设置值
     */
    public static <E> E getDefaultConfig(String name, Class<E> cls) {
        return getDefaultInner(name, cls);
    }

    /**
     * 获取默认设置组
     * 
     * @param name 配置名称
     * @param cls 目标类型，一般为八种基本类型和字符串
     * @return 目标类型数组的设置值，使用逗号分隔配置多个值
     */
    public static <E> E[] getDefaultConfigs(String name, Class<E> cls) {
        return getDefaultInners(name, cls);
    }

    /**
     * 获取默认组件，将组件类型的类名称作为key值从属性文件中获取相应配置的实现类，然后实例化并返回
     * 
     * @param cls 组件类型，一般为接口
     * @return 配置的组件实现类
     */
    public static <T> T getDefaultComponent(Class<T> cls) {
        return getDefaultInner(cls.getName(), cls);
    }

    /**
     * 获取默认组件组，将组件类型的类名称作为key值从属性文件中获取相应配置的实现类，然后实例化并返回
     * 
     * @param cls 组件类型，一般为接口
     * @return 配置的组件实现类组，使用逗号分隔配置多个值
     */
    public static <T> T[] getDefaultComponents(Class<T> cls) {
        return getDefaultInners(cls.getName(), cls);
    }

    /**
     * 获取默认组件，将组件类型的类名称加井号#，再加上name作为key值从属性文件中获取相应配置的实现类，然后实例化并返回
     * 
     * @param cls 组件类型，一般为接口
     * @param name 名称
     * @return 配置的组件实现类组
     */
    public static <T> T getDefaultComponent(Class<T> cls, String name) {
        return getDefaultInner(cls.getName() + "#" + name, cls);
    }

    /**
     * 获取默认组件组，将组件类型的类名称加井号#，再加上name作为key值从属性文件中获取相应配置的实现类，然后实例化并返回
     * 
     * @param cls 组件类型，一般为接口
     * @param name 名称
     * @return 配置的组件实现类组，使用逗号分隔配置多个值
     */
    public static <T> T[] getDefaultComponents(Class<T> cls, String name) {
        return getDefaultInners(cls.getName() + "#" + name, cls);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static <T> T getDefaultInner(String name, Class<T> cls) {
        if (!map.containsKey(name)) {
            synchronized (map) {
                if (!map.containsKey(name)) {
                    T rs = null;
                    String vp = properties.getProperty(name);
                    if (CoreUtils.isBlank(vp)) {
                        if (cls.equals(String.class)) {
                            rs = cls.cast("");
                        } else {
                            rs = null;
                        }
                    } else if (String.class.equals(cls)) {
                        rs = cls.cast(vp);
                    } else if (boolean.class.equals(cls) || Boolean.class.equals(cls)) {
                        Boolean b = CoreUtils.string2Boolean(vp);
                        return (T) b;
                    } else if (cls.isPrimitive()) {
                        rs = CoreUtils.convert(vp, cls);
                    } else if (Number.class.isAssignableFrom(int.class)) {
                        rs = cls.cast(CoreUtils.convertStringToTargetClass(vp, (Class<? extends Number>) cls));
                    } else if (Enum.class.isAssignableFrom(cls)) {
                        Class enumClass = (Class) cls;
                        //rs = cls.cast(Enum.valueOf((Class) enumClass, vp));
                    } else {
                        try {
                            rs = cls.cast(ReflectUtils.newInstance(Class.forName(vp)));
                        } catch (ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    properties.remove(name);
                    map.put(name, rs);
                }
            }
        }
        return (T) map.get(name);
    }

    @SuppressWarnings("unchecked")
    private static <T> T[] getDefaultInners(String name, Class<T> cls) {
        if (!map.containsKey(name)) {
            synchronized (map) {
                if (!map.containsKey(name)) {
                    T[] rs = null;
                    String vp = properties.getProperty(name);
                    if (CoreUtils.isBlank(vp)) {
                        rs = null;
                        CommonLogger.debug("the default value of [" + name + "] is null");
                    } else if ("[]".equals(vp)) {
                        rs = (T[]) Array.newInstance(cls, 0);
                    } else {
                        String[] names = vp.split("\\s*,\\s*");
                        rs = (T[]) Array.newInstance(cls, names.length);
                        for (int i = 0, l = names.length; i < l; i++) {
                            if (cls.equals(String.class)) {
                                rs[i] = cls.cast(names[i]);
                            } else {
                                try {
                                    rs[i] = cls.cast(ReflectUtils.newInstance(Class.forName(names[i])));
                                } catch (ClassNotFoundException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    }
                    properties.remove(name);
                    map.put(name, rs);
                }
            }
        }
        return (T[]) map.get(name);
    }

    /**
     * 加载默认配置
     */
    private synchronized static void loadDefaults() {
        try {
            /**
             * 加载平台包下面的defaults.properties文件
             */
            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources("classpath*:com/itouch8/pump/**/defaults.properties");
            if (null != resources) {
                List<Properties> list = new ArrayList<Properties>();
                for (Resource resource : resources) {
                    InputStream input = null;
                    try {
                        Properties properties = new Properties();
                        input = resource.getInputStream();
                        properties.load(input);
                        list.add(properties);
                    } catch (Exception e) {
                        // ignore
                    } finally {
                        CoreUtils.closeQuietly(input);
                    }
                }
                /**
                 * 根据配置文件中的order排序
                 */
                Collections.sort(list, new Comparator<Properties>() {
                    @Override
                    public int compare(Properties o1, Properties o2) {
                        return Integer.parseInt(o2.getProperty("order", "0")) - Integer.parseInt(o1.getProperty("order", "0"));
                    }
                });
                for (Properties p : list) {
                    p.remove("order");
                    properties.putAll(p);
                }
            }
        } catch (IOException ignore) {
        }
    }

    /**
     * 加载需要将多个文件组合在一起的默认配置
     */
    private synchronized static void loadCombineDefaults() {
        try {
            /**
             * 加载平台包下面的combineDefaults.properties文件
             */
            Map<String, Set<String>> combines = new HashMap<String, Set<String>>();
            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources("classpath*:com/itouch8/pump/**/combineDefaults.properties");
            if (null != resources) {
                for (Resource resource : resources) {
                    InputStream input = null;
                    try {
                        Properties properties = new Properties();
                        input = resource.getInputStream();
                        properties.load(input);
                        for (String key : properties.stringPropertyNames()) {
                            String value = properties.getProperty(key);
                            if (!CoreUtils.isBlank(value)) {
                                String[] values = value.split("\\s*,\\s*");
                                for (String v : values) {
                                    if (!CoreUtils.isBlank(v)) {
                                        Set<String> l = combines.get(key);
                                        if (null == l) {
                                            l = new LinkedHashSet<String>();
                                            combines.put(key, l);
                                        }
                                        l.add(v);
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        // ignore
                    } finally {
                        CoreUtils.closeQuietly(input);
                    }
                }
                for (String key : combines.keySet()) {
                    Set<String> l = combines.get(key);
                    if (null != l && !l.isEmpty()) {
                        StringBuffer sb = new StringBuffer();
                        for (String s : l) {
                            sb.append(",").append(s);
                        }
                        properties.put(key, sb.substring(1));
                    }
                }
            }
        } catch (IOException ignore) {
        }
    }
}
