package com.itouch8.pump.core.util;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.objenesis.SpringObjenesis;
import org.springframework.util.NumberUtils;
import org.springframework.util.ObjectUtils;

import com.itouch8.pump.ReturnCodes;
import com.itouch8.pump.core.util.bean.IContextBeanOperateWrapper;
import com.itouch8.pump.core.util.config.BaseConfig;
import com.itouch8.pump.core.util.env.EnvConsts;
import com.itouch8.pump.core.util.exception.Throw;
import com.itouch8.pump.core.util.filter.method.IMethodFilter;
import com.itouch8.pump.core.util.filter.method.impl.AnnoMethodFilter;
import com.itouch8.pump.core.util.filter.type.ITypeFilter;
import com.itouch8.pump.core.util.filter.type.impl.AnnoClassFilter;
import com.itouch8.pump.core.util.filter.type.impl.ParentClassFilter;
import com.itouch8.pump.core.util.locale.ILocaleMessageResolver;
import com.itouch8.pump.core.util.locale.ILocaleResolver;
import com.itouch8.pump.core.util.logger.CommonLogger;


final public class CoreUtils {

    
    private static final SpringObjenesis objenesis = new SpringObjenesis();
    
    private static final Pattern localeReplacePattern = Pattern.compile("\\s*\\{\\s*\\d+\\s*}\\s*");

    
    private static final int[] MAX_DAY_OF_MONTH = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    
    private static final ThreadLocal<Map<String, Object>> cache = new InheritableThreadLocal<Map<String, Object>>() {
        @Override
        protected Map<String, Object> initialValue() {
            return new HashMap<String, Object>();
        }

        @SuppressWarnings("unchecked")
        @Override
        protected Map<String, Object> childValue(Map<String, Object> parentValue) {
            if (parentValue != null) {
                return (Map<String, Object>) ((HashMap<String, Object>) parentValue).clone();
            } else {
                return null;
            }
        }
    };

    
    public static boolean isBlank(CharSequence cs) {
        return StringUtils.isBlank((String) cs);
    }

    
    public static boolean isEmpty(Object obj) {
        return ObjectUtils.isEmpty(obj);
    }

    
    public static String formatWhitespace(String src) {
        if (null == src) {
            return null;
        }
        StringTokenizer st = new StringTokenizer(src);
        StringBuilder builder = new StringBuilder();
        while (st.hasMoreTokens()) {
            builder.append(st.nextToken()).append(" ");
        }
        return builder.toString();
    }

    
    public static String convertToCamel(String str) {
        if (null != str) {
            StringBuilder rs = new StringBuilder();
            boolean upper = false, first = true;
            for (char ch : str.trim().toCharArray()) {
                if (ch == '-' || ch == '_') {
                    upper = !first;
                } else {
                    rs.append(upper ? Character.toUpperCase(ch) : Character.toLowerCase(ch));
                    upper = false;
                    first = false;
                }
            }
            return rs.toString();
        }
        return null;
    }

    
    public static List<String> splitToList(String src, String separator) {
        List<String> l = new ArrayList<String>();
        if (null == src) {
            return null;
        } else if (null == separator) {
            l.add(src);
        } else {
            for (int i = src.indexOf(separator), o = separator.length(); i != -1; i = src.indexOf(separator)) {
                l.add(src.substring(0, i));
                src = src.substring(i + o);
            }
            if (!isBlank(src)) {
                l.add(src);
            }
        }
        return l;
    }

    
    public static List<String> splitToList(String src, String separator, int minSize, String defaultString) {
        List<String> l = splitToList(src, separator);
        if (null == l) {
            l = new ArrayList<String>();
        }
        for (int i = minSize - l.size() - 1; i >= 0; i--) {
            l.add(defaultString);
        }
        return l;
    }

    
    public static String[] split(String src, String separator) {
        List<String> l = splitToList(src, separator);
        return null == l ? null : l.toArray(new String[l.size()]);
    }

    
    public static String[] split(String src, String separator, int minLength, String defaultString) {
        List<String> l = splitToList(src, separator, minLength, defaultString);
        return null == l ? null : l.toArray(new String[l.size()]);
    }

    
    public static String join(List<?> list, String separator) {
        if (null == list || list.isEmpty()) {
            return null;
        }
        if (null == separator) {
            separator = ",";
        }
        StringBuffer sb = new StringBuffer();
        for (Object obj : list) {
            if (null != obj) {
                sb.append(separator).append(obj.toString());
            }
        }
        return sb.substring(separator.length()).toString();
    }

    
    public static String join(Object[] arr, String separator) {
        if (null == arr || arr.length == 0) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        for (Object obj : arr) {
            if (null != obj) {
                sb.append(separator).append(obj.toString());
            }
        }
        return sb.substring(separator.length()).toString();
    }

    
    public static String rightPad(final String str, final int size, final String padStr) {
        return StringUtils.rightPad(str, size, padStr);
    }

    
    public static boolean safeEquals(String str1, String str2) {
        return str1 == null ? str2 == null : str1.equals(str2);
    }

    
    public static boolean safeEqualsIgnoreCase(String str1, String str2) {
        return str1 == null ? str2 == null : str1.equalsIgnoreCase(str2);
    }

    
    public static boolean string2Boolean(String str) {
        if (null == str) {
            return false;
        }
        str = str.trim();
        return "1".equals(str) || "Y".equalsIgnoreCase(str) || "TRUE".equalsIgnoreCase(str) || "ON".equalsIgnoreCase(str);
    }

    
    @SuppressWarnings("unchecked")
    public static <T> T cast(Object obj) {
        if (obj != null) {
            try {
                return (T) obj;
            } catch (Exception e) {
                throw new ClassCastException("Cannot cast " + obj.getClass().getName() + " to <T>...");
            }
        }
        return null;
    }

    
    @SuppressWarnings("unchecked")
    public static <E> List<E> convertToList(Object source, Class<E> elementType) {
        if (null == source) {
            return null;
        } else if (source.getClass().isArray()) {
            List<E> list = new ArrayList<E>();
            Class<?> c = source.getClass();
            if (c.equals(int[].class)) {
                int[] arr = (int[]) source;
                if (arr.length == 0) {
                    return Collections.<E>emptyList();
                } else if (elementType.isAssignableFrom(Integer.class)) {
                    for (Integer a : arr) {
                        list.add((E) a);
                    }
                    return list;
                } else {
                    throw new ClassCastException("can not convert the list, the element type is int, but the target type is " + elementType);
                }
            } else if (c.equals(short[].class)) {
                short[] arr = (short[]) source;
                if (arr.length == 0) {
                    return Collections.<E>emptyList();
                } else if (elementType.isAssignableFrom(Short.class)) {
                    for (Short a : arr) {
                        list.add((E) a);
                    }
                    return list;
                } else {
                    throw new ClassCastException("can not convert the list, the element type is short, but the target type is " + elementType);
                }
            } else if (c.equals(long[].class)) {
                long[] arr = (long[]) source;
                if (arr.length == 0) {
                    return Collections.<E>emptyList();
                } else if (elementType.isAssignableFrom(Long.class)) {
                    for (Long a : arr) {
                        list.add((E) a);
                    }
                    return list;
                } else {
                    throw new ClassCastException("can not convert the list, the element type is long, but the target type is " + elementType);
                }
            } else if (c.equals(byte[].class)) {
                byte[] arr = (byte[]) source;
                if (arr.length == 0) {
                    return Collections.<E>emptyList();
                } else if (elementType.isAssignableFrom(Byte.class)) {
                    for (Byte a : arr) {
                        list.add((E) a);
                    }
                    return list;
                } else {
                    throw new ClassCastException("can not convert the list, the element type is byte, but the target type is " + elementType);
                }
            } else if (c.equals(boolean[].class)) {
                boolean[] arr = (boolean[]) source;
                if (arr.length == 0) {
                    return Collections.<E>emptyList();
                } else if (elementType.isAssignableFrom(Boolean.class)) {
                    for (Boolean a : arr) {
                        list.add((E) a);
                    }
                    return list;
                } else {
                    throw new ClassCastException("can not convert the list, the element type is boolean, but the target type is " + elementType);
                }
            } else if (c.equals(char[].class)) {
                char[] arr = (char[]) source;
                if (arr.length == 0) {
                    return Collections.<E>emptyList();
                } else if (elementType.isAssignableFrom(Character.class)) {
                    for (Character a : arr) {
                        list.add((E) a);
                    }
                    return list;
                } else {
                    throw new ClassCastException("can not convert the list, the element type is char, but the target type is " + elementType);
                }
            } else if (c.equals(float[].class)) {
                float[] arr = (float[]) source;
                if (arr.length == 0) {
                    return Collections.<E>emptyList();
                } else if (elementType.isAssignableFrom(Float.class)) {
                    for (Float a : arr) {
                        list.add((E) a);
                    }
                    return list;
                } else {
                    throw new ClassCastException("can not convert the list, the element type is float, but the target type is " + elementType);
                }
            } else if (c.equals(double[].class)) {
                double[] arr = (double[]) source;
                if (arr.length == 0) {
                    return Collections.<E>emptyList();
                } else if (elementType.isAssignableFrom(Double.class)) {
                    for (Double a : arr) {
                        list.add((E) a);
                    }
                    return list;
                } else {
                    throw new ClassCastException("can not convert the list, the element type is double, but the target type is " + elementType);
                }
            } else {
                Object[] arr = (Object[]) source;
                if (arr.length == 0) {
                    return Collections.<E>emptyList();
                } else {
                    Object first = arr[0];
                    if (elementType.isAssignableFrom(first.getClass())) {
                        for (Object a : arr) {
                            list.add((E) a);
                        }
                        return list;
                    } else {
                        throw new ClassCastException("can not convert the list, the element type is " + first.getClass() + ", but the target type is " + elementType);
                    }
                }
            }
        } else if (source instanceof List) {
            List<E> list = (List<E>) source;
            if (list.isEmpty()) {
                return Collections.<E>emptyList();
            } else {
                Object first = list.get(0);
                if (elementType.isAssignableFrom(first.getClass())) {
                    return list;
                } else {
                    throw new ClassCastException("can not convert the list, the element type is " + first.getClass() + ", but the target type is " + elementType);
                }
            }
        } else if (source instanceof Enumeration) {
            Enumeration<E> i = (Enumeration<E>) source;
            if (!i.hasMoreElements()) {
                return Collections.<E>emptyList();
            } else {
                Object first = i.nextElement();
                if (elementType.isAssignableFrom(first.getClass())) {
                    List<E> rs = new ArrayList<E>();
                    rs.add((E) first);
                    while (i.hasMoreElements()) {
                        rs.add(i.nextElement());
                    }
                    return rs;
                } else {
                    throw new ClassCastException("can not convert the list, the element type is " + first.getClass() + ", but the target type is " + elementType);
                }
            }
        } else if (source instanceof Iterator) {
            Iterator<E> i = (Iterator<E>) source;
            if (!i.hasNext()) {
                return Collections.<E>emptyList();
            } else {
                Object first = i.next();
                if (elementType.isAssignableFrom(first.getClass())) {
                    List<E> rs = new ArrayList<E>();
                    rs.add((E) first);
                    while (i.hasNext()) {
                        rs.add(i.next());
                    }
                    return rs;
                } else {
                    throw new ClassCastException("can not convert the list, the element type is " + first.getClass() + ", but the target type is " + elementType);
                }
            }
        } else if (source instanceof Iterable) {
            return convertToList(((Iterable<?>) source).iterator(), elementType);
        } else if (source instanceof Map) {
            return convertToList(((Map<?, ?>) source).values(), elementType);
        } else {
            return convertToList(new Object[] {source}, elementType);
        }
    }

    
    public static <T extends Number> T convertNumberToTargetClass(Number number, Class<T> targetClass) {
        return NumberUtils.convertNumberToTargetClass(number, targetClass);
    }

    
    public static <T extends Number> T convertStringToTargetClass(String text, Class<T> targetClass) {
        return NumberUtils.parseNumber(text, targetClass);
    }

    
    public static <T> T convert(Object source, Class<T> targetType) {
        return BaseConfig.getConversionService().convert(source, targetType);
    }

    
    public static String convertIp(String ip) {
        if ("127.0.0.1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip) || "0.0.0.0".equals(ip)) {
            return EnvConsts.LOCALE_HOST;
        } else {
            return ip;
        }
    }

    
    public static String format(String pattern, Object... arguments) {
        String msg = null;
        if (null == arguments || 0 == arguments.length) {
            msg = pattern;
        } else {
            msg = MessageFormat.format(pattern, arguments);
        }
        if (null != msg) {
            msg = localeReplacePattern.matcher(msg).replaceAll("");
        }
        return msg;
    }

    
    public static String getMessage(String code, Object... args) {
        return getMessage(code, null, null, args);
    }

    
    public static String getMessage(String code, Locale locale, Object... args) {
        return getMessage(code, null, locale, args);
    }

    
    public static String getMessage(String code, String defaultMessage, Locale locale, Object... args) {
        ILocaleMessageResolver localeResolver = BaseConfig.getLocaleMessageResolver();
        String msg = localeResolver.getMessage(code, defaultMessage, locale, args);
        if (null != msg) {
            msg = localeReplacePattern.matcher(msg).replaceAll("");
        }
        return msg;
    }

    
    public static Locale getCurrentLocale() {
        Locale locale = null;
        ILocaleResolver lr = BaseConfig.getLocaleResolver();
        if (null != lr) {
            locale = lr.getLocale();
        }
        return null == locale ? EnvConsts.SYSTEM_LOCALE : locale;
    }

    
    public static void setCurrentLocale(Locale locale) {
        ILocaleResolver lr = BaseConfig.getLocaleResolver();
        if (null != lr) {
            lr.setLocale(locale);
        } else {
            Locale.setDefault(locale);
        }
    }

    
    public static Locale toLocale(String str) {
        return LocaleUtils.toLocale(str);
    }

    
    public static <T> T newInstance(Class<T> cls) {
        try {
            if (null == cls) {
                return null;
            }
            return BaseConfig.getObjectFactory().create(cls);
        } catch (Exception e) {
            CommonLogger.debug("can not create instance by default constructor, try use the objenesis method, the class : " + cls);
            return objenesis.newInstance(cls);
        }
    }

    
    public static Object newInstance(String className) {
        if (null == className) {
            return null;
        }
        try {
            return BaseConfig.getObjectFactory().create(className);
        } catch (Exception e) {
            CommonLogger.debug("can not create instance by default constructor, try use the objenesis method, the class : " + className);
            Class<?> cls = forName(className);
            return objenesis.newInstance(cls);
        }
    }

    
    public static Class<?> forName(String name) {
        return forName(name, null);
    }

    
    public static Class<?> forName(String name, ClassLoader classLoader) {
        try {
            if (null == classLoader) {
                classLoader = getDefaultClassLoader();
            }
            return org.springframework.util.ClassUtils.forName(name, classLoader);
        } catch (Exception ex) {
            throw Throw.createRuntimeException(ReturnCodes.SYSTEM_ERROR.code,"pump.exception",ex, name);
        }
    }

    
    public static List<Class<?>> convertClassNamesToClasses(List<String> classNames) {
        if (classNames == null) {
            return null;
        }
        List<Class<?>> classes = new ArrayList<Class<?>>(classNames.size());
        ClassLoader classLoader = getDefaultClassLoader();
        for (String className : classNames) {
            try {
                classes.add(forName(className, classLoader));
            } catch (Exception ex) {
                classes.add(null);
            }
        }
        return classes;
    }

    
    public static List<String> convertClassesToClassNames(List<Class<?>> classes) {
        if (classes == null) {
            return null;
        }
        List<String> classNames = new ArrayList<String>(classes.size());
        for (Class<?> cls : classes) {
            if (cls == null) {
                classNames.add(null);
            } else {
                classNames.add(cls.getName());
            }
        }
        return classNames;
    }

    
    public static ClassLoader getDefaultClassLoader() {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        } catch (Throwable ex) {
        }
        if (cl == null) {
            cl = CoreUtils.class.getClassLoader();
            if (cl == null) {
                try {
                    cl = ClassLoader.getSystemClassLoader();
                } catch (Throwable ex) {
                }
            }
        }
        return cl;
    }

    
    public static Field findField(Class<?> clazz, String name) {
        return findField(clazz, name, null);
    }

    
    public static Field findField(Class<?> clazz, String name, Class<?> type) {
        Class<?> searchType = clazz;
        while (Object.class != searchType && searchType != null) {
            Field[] fields = searchType.getDeclaredFields();
            for (Field field : fields) {
                if ((name == null || name.equals(field.getName())) && (type == null || type.equals(field.getType()))) {
                    return field;
                }
            }
            searchType = searchType.getSuperclass();
        }
        return null;
    }

    
    public static Resource getResource(String location) {
        return getResource(null, location);
    }

    
    public static Resource getResource(Environment environment, String location) {
        if (null != environment) {
            location = environment.resolveRequiredPlaceholders(location);
        }
        File file = new File(location);
        if (file.exists()) {
            return new FileSystemResource(file);
        } else {
            ResourcePatternResolver loader = BaseConfig.getResourcePatternResolver();
            return loader.getResource(location);
        }
    }

    
    public static Set<Resource> getResources(String locationPattern) {
        return getResources(null, new String[] {locationPattern});
    }

    
    public static Set<Resource> getResources(String[] locationPatterns) {
        return getResources(null, locationPatterns);
    }

    
    public static Set<Resource> getResources(Environment environment, String[] locationPatterns) {
        try {
            Set<Resource> resources = new LinkedHashSet<Resource>();
            ResourcePatternResolver loader = BaseConfig.getResourcePatternResolver();
            for (String location : locationPatterns) {
                if (null != environment) {
                    location = environment.resolveRequiredPlaceholders(location);
                }
                for (Resource resource : loader.getResources(location)) {
                    resources.add(resource);
                }
            }
            return resources;
        } catch (IOException e) {
            throw Throw.createRuntimeException(e);
        }
    }

    
    @SuppressWarnings("unchecked")
    public static <E> Set<Class<? extends E>> scanClassesByParentCls(String basePackage, Class<E> parent) {
        Set<Class<?>> set = scanClasses(basePackage, new ParentClassFilter(parent));
        Set<Class<? extends E>> rs = new LinkedHashSet<Class<? extends E>>();
        if (null != set && !set.isEmpty()) {
            for (Class<?> s : set) {
                rs.add((Class<? extends E>) s);
            }
        }
        return rs;
    }

    
    public static <E extends Annotation> Set<Class<?>> scanClasses(String basePackage, Class<E> annoCls) {
        return scanClasses(basePackage, new AnnoClassFilter(annoCls));
    }

    
    public static Set<Class<?>> scanClasses(String basePackage, ITypeFilter filter) {
        return BaseConfig.getScan().scanClasses(basePackage, filter);
    }

    
    public static <E extends Annotation> Set<Method> scanMethods(String basePackage, Class<E> annoCls) {
        return scanMethods(basePackage, new AnnoMethodFilter(annoCls));
    }

    
    public static Set<Method> scanMethods(String basePackage, IMethodFilter filter) {
        return BaseConfig.getScan().scanMethods(basePackage, filter);
    }

    
    public static Object getProperty(Object bean, String property) {
        return getBeanOperateWrapper().getProperty(bean, property);
    }

    
    public static <E> E getProperty(Object bean, String property, Class<E> resultType) {
        return getBeanOperateWrapper().getProperty(bean, property, resultType);
    }

    
    public static void setProperty(Object bean, String property, Object value) {
        getBeanOperateWrapper().setProperty(bean, property, value);
    }

    
    public static void removeProperty(Object bean, String property) {
        getBeanOperateWrapper().removeProperty(bean, property);
    }

    
    public static Object getProperty(Object bean, String expression, Map<String, Object> context) {
        return getBeanOperateWrapper().getProperty(bean, expression, context);
    }

    
    public static <E> E getProperty(Object bean, String expression, Map<String, Object> context, Class<E> resultType) {
        return getBeanOperateWrapper().getProperty(bean, expression, context, resultType);
    }

    
    public static void setProperty(Object bean, String expression, Object value, Map<String, Object> context) {
        getBeanOperateWrapper().setProperty(bean, expression, value, context);
    }

    
    public static Object[] getProperties(Object bean, String[] expressions) {
        return getProperties(bean, expressions, (Map<String, Object>) null);
    }

    
    public static Object[] getProperties(Object bean, String[] expressions, Map<String, Object> context) {
        if (null == expressions || null == bean || 0 == expressions.length) {
            return new Object[0];
        }
        int length = expressions.length;
        Object[] result = new Object[length];
        for (int i = 0; i < length; i++) {
            result[i] = getProperty(bean, expressions[i], context);
        }
        return result;
    }

    private static IContextBeanOperateWrapper getBeanOperateWrapper() {
        return BaseConfig.getBeanOperateWrapper();
    }

    
    public static void putThreadCache(String key, Object value) {
        cache.get().put(key, value);
    }

    
    public static <E> E getThreadCache(String key, Class<E> cls) {
        return cls.cast(cache.get().get(key));
    }

    
    public static void removeThreadCache(String key) {
        cache.get().remove(key);
    }

    
    public static void clearThreadCache() {
        cache.get().clear();
        cache.remove();
    }

    
    public static void closeQuietly(Closeable... closeable) {
        if (null != closeable && closeable.length >= 1) {
            for (Closeable c : closeable) {
                if (null != c) {
                    try {
                        c.close();
                    } catch (IOException i) {
                    }
                }
            }
        }
    }

    
    public static String getDate() {
        return getFormatDate(new Date(), BaseConfig.getDateFormat());
    }

    
    public static String getTime() {
        return getFormatDate(new Date(), BaseConfig.getTimeFormat());
    }

    
    public static String getDateAndTime() {
        return getDateAndTime(new Date());
    }

    
    public static String getDateAndTime(Date date) {
        return getFormatDate(date, BaseConfig.getDatetimeFormat());
    }

    
    public static String getFormatDate(Date date, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        String rs = df.format(date);
        return rs;
    }

    
    public static String dateCalculate(String date, int mYear, int mMonth, int mDay) {
        return dateCalculate(date, BaseConfig.getDateFormat(), mYear, mMonth, mDay);
    }

    
    public static String dateCalculate(String date, String format, int mYear, int mMonth, int mDay) {
        try {
            DateFormat fo = new SimpleDateFormat(format);
            Date sDate = fo.parse(date);
            Date rDate = dateCalculate(sDate, mYear, mMonth, mDay);
            return fo.format(rDate);
        } catch (ParseException e) {
            throw Throw.createRuntimeException(e);
        }
    }

    
    public static Date dateCalculate(Date date, int mYear, int mMonth, int mDay) {
        Calendar tempCal = Calendar.getInstance();
        tempCal.setTime(date);
        tempCal.add(Calendar.YEAR, mYear);
        tempCal.add(Calendar.MONTH, mMonth);
        tempCal.add(Calendar.DATE, mDay);
        return tempCal.getTime();
    }

    
    public static int dayOfYear(int year, int month, int day) {
        int day_of_year;
        if (isLeapYear(year)) {
            day_of_year = ((275 * month) / 9) - ((month + 9) / 12) + day - 30;
        } else {
            day_of_year = ((275 * month) / 9) - (((month + 9) / 12) << 1) + day - 30;
        }
        return day_of_year;
    }

    
    public static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0;
    }

    
    public static int getMaxDayOfMonth(int year, int month) {
        if ((month < 1) || (month > 12)) {
            throw new IllegalArgumentException("Invalid month: " + month);
        }
        if (month == 2) {
            return isLeapYear(year) ? 29 : 28;
        }
        if ((year == 1582) && (month == 10)) {
            return 21;
        }
        return MAX_DAY_OF_MONTH[month];
    }

    
    public static long getTime(String datetime, String datetimeFormat) {
        try {
            DateFormat fo = new SimpleDateFormat(datetimeFormat);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(fo.parse(datetime));
            return calendar.getTimeInMillis();
        } catch (ParseException e) {
            throw Throw.createRuntimeException(e);
        }
    }

    
    public synchronized static int runOSCommand(String... cmd) {
        int result = -1;
        BufferedReader bufferReader = null;
        BufferedReader bufferErrorReader = null;
        Process pid = null;
        try {
            if (0 == cmd.length) {
                pid = Runtime.getRuntime().exec(cmd[0]);
            } else {
                pid = Runtime.getRuntime().exec(cmd);
            }

            bufferReader = new BufferedReader(new InputStreamReader(pid.getInputStream()));
            while (bufferReader.readLine() != null) {
                // 清除process的流，以免造成process阻塞
            }

            bufferErrorReader = new BufferedReader(new InputStreamReader(pid.getErrorStream(), EnvConsts.OS_ENCODING));
            StringBuffer error = new StringBuffer();
            String line = null;
            while ((line = bufferErrorReader.readLine()) != null) {
                // 清除process的流，以免造成process阻塞
                error.append(EnvConsts.LINE_SEPARATOR).append(line);
            }
            if (!CoreUtils.isBlank(error)) {
                CommonLogger.error(error.toString());
            }
            pid.waitFor();
            result = pid.exitValue();
            if (0 != result && 2 != result) {
                Throw.throwRuntimeException("执行" + StringUtils.join(cmd, " ") + "命令异常");
            }
        } catch (Exception ioe) {
            Throw.throwRuntimeException("执行" + StringUtils.join(cmd, " ") + "命令异常", ioe);
        } finally {
            CoreUtils.closeQuietly(bufferReader, bufferErrorReader);
            if (pid != null) {
                pid.destroy();
            }
        }
        return result;
    }
}
