package com.itouch8.pump.core.util.cache;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Pattern;

import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;

import com.itouch8.pump.core.util.CoreUtils;
import com.itouch8.pump.core.util.config.BaseConfig;
import com.itouch8.pump.core.util.exception.Throw;
import com.itouch8.pump.core.util.exception.meta.ExceptionCodes;


public class Caches {

    
    private static final String PUMP_CACHE_KEY = "pump";

    
    private static final Pattern PUMP_PACKAGE_PATTERN = Pattern.compile("^com\\.itouch8\\.pump\\..*");

    private static Field CONCURRENT_CACHE_MAP_FIELD;
    static {
        try {
            CONCURRENT_CACHE_MAP_FIELD = ConcurrentMapCacheManager.class.getDeclaredField("cacheMap");
            CONCURRENT_CACHE_MAP_FIELD.setAccessible(true);
        } catch (Exception e) {
        }
    }

    private static CacheManager getCacheManager() {
        return BaseConfig.getCacheManager();
    }

    
    public static void assertNotPumpCache(String cacheName) {
        if (isPumpCache(cacheName)) {
            Throw.throwRuntimeException(ExceptionCodes.YT010006, "", cacheName);
        }
    }

    
    public static void checkCacheName(String cacheName, Class<?> visitClass) {
        checkCacheName(cacheName, visitClass.getName());
    }

    
    public static void checkCacheName(String cacheName, String visitClassName) {
        if (isPumpCache(cacheName) && !isPumpClass(visitClassName)) {
            Throw.throwRuntimeException(ExceptionCodes.YT010006, visitClassName, cacheName);
        }
    }

    
    public static void clearCacheAndContainer(CacheManager cacheManager, String cacheName) {
        if (null == cacheManager || null == cacheName) {
            return;
        }
        Cache cache = cacheManager.getCache(cacheName);
        if (null == cache) {
            return;
        }
        cache.clear();
        if (cacheManager instanceof ConcurrentMapCacheManager) {
            try {
                @SuppressWarnings("unchecked")
                ConcurrentMap<String, Cache> cacheMap = (ConcurrentMap<String, Cache>) CONCURRENT_CACHE_MAP_FIELD.get(cacheManager);
                cacheMap.remove(cacheName);
            } catch (Exception e) {
            }
        }
    }

    
    public static void clearAllCache() {
        clearAllCache0(2);
    }

    
    public static Object get(Object key) {
        Cache cache = getDefaultCache();
        ValueWrapper wrapper = cache.get(key);
        return null == wrapper ? null : wrapper.get();
    }

    
    public static <E> E get(Object key, Class<E> cls) {
        Cache cache = getDefaultCache();
        return cache.get(key, cls);
    }

    
    public static void put(Object key, Object value) {
        Cache cache = getDefaultCache();
        cache.put(key, value);
    }

    
    public static Object putIfAbsent(Object key, Object value) {
        Cache cache = getDefaultCache();
        ValueWrapper wrapper = cache.putIfAbsent(key, value);
        return null == wrapper ? null : wrapper.get();
    }

    
    public static void remove(Object key) {
        Cache cache = getDefaultCache();
        cache.evict(key);
    }

    
    public static void clear() {
        Cache cache = getDefaultCache();
        cache.clear();
    }

    
    public static Collection<String> getCacheNames() {
        return getCacheNames0(2);
    }

    
    public static Cache getCache(String cacheName) {
        return getCache0(cacheName, 2);
    }

    
    public static Cache getCache(Class<?> cls) {
        return getCache0(cls.getName(), 2);
    }

    
    public static Cache getCache(Class<?> cls, String name) {
        return getCache0(cls.getName() + "###" + name, 2);
    }

    
    public static boolean isPumpCache(String cacheName) {
        return null != cacheName && cacheName.toLowerCase().indexOf(PUMP_CACHE_KEY) != -1;
    }

    
    public static boolean isPumpClass(String className) {
        return PUMP_PACKAGE_PATTERN.matcher(className).find();
    }

    
    private static Cache getCache0(String cacheName, int stackDeep) {
        CacheManager delegate = getCacheManager();
        if (null == delegate) {
            return null;
        }
        String visitClassName = new Exception().getStackTrace()[stackDeep].getClassName();
        checkCacheName(cacheName, visitClassName);
        return delegate.getCache(cacheName);
    }

    
    private static Collection<String> getCacheNames0(int stackDeep) {
        CacheManager delegate = getCacheManager();
        if (null == delegate) {
            return null;
        }

        Collection<String> names = delegate.getCacheNames();
        if (null == names || names.isEmpty()) {
            return names;
        } else {
            Collection<String> nms = new ArrayList<String>();
            String visitClassName = new Exception().getStackTrace()[stackDeep].getClassName();
            boolean isPump = isPumpClass(visitClassName);
            for (String name : names) {
                if (isPump || !isPumpCache(name)) {// 是平台访问，或者不是平台缓存
                    nms.add(name);
                }
            }
            return nms;
        }
    }

    
    private static void clearAllCache0(int stackDeep) {
        CacheManager delegate = getCacheManager();
        if (null == delegate) {
            return;
        }

        Collection<String> names = delegate.getCacheNames();
        if (null == names || names.isEmpty()) {
            return;
        } else {
            String visitClassName = new Exception().getStackTrace()[stackDeep].getClassName();
            boolean isPump = isPumpClass(visitClassName);
            for (String name : names) {
                if (isPump || !isPumpCache(name)) {// 是平台访问，或者不是平台缓存
                    clearCacheAndContainer(delegate, name);
                }
            }
        }
    }

    
    private static Cache getDefaultCache() {
        String cacheName = BaseConfig.getDefaultCacheName();
        if (CoreUtils.isBlank(cacheName)) {
            Throw.throwRuntimeException(ExceptionCodes.YT010007);
        }
        Cache cache = getCache0(cacheName, 3);
        if (null == cache) {
            Throw.throwRuntimeException(ExceptionCodes.YT010008, cacheName);
        }
        return cache;
    }
}
