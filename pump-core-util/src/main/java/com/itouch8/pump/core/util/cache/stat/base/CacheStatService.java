package com.itouch8.pump.core.util.cache.stat.base;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.util.ClassUtils;

import com.itouch8.pump.core.util.cache.Caches;
import com.itouch8.pump.core.util.cache.stat.ICacheInfo;
import com.itouch8.pump.core.util.cache.stat.ICacheStatService;
import com.itouch8.pump.core.util.cache.stat.concurrent.ConcurrentCacheInfo;
import com.itouch8.pump.core.util.cache.stat.ehcache.EhcacheCacheInfo;
import com.itouch8.pump.core.util.cache.stat.redis.RedisCacheInfo;
import com.itouch8.pump.core.util.config.BaseConfig;
import com.itouch8.pump.core.util.env.EnvConsts;
import com.itouch8.pump.core.util.logger.CommonLogger;


public class CacheStatService implements ICacheStatService {

    private static final Map<String, Class<? extends ICacheInfo>> defaultCacheInfoMapping = new HashMap<String, Class<? extends ICacheInfo>>();
    private final Map<String, Constructor<? extends ICacheInfo>> constructors = new HashMap<String, Constructor<? extends ICacheInfo>>();

    static {
        // ConcurrentMap
        defaultCacheInfoMapping.put(ConcurrentHashMap.class.getName(), ConcurrentCacheInfo.class);
        ClassLoader classLoader = CacheStatService.class.getClassLoader();

        // Ehcache
        String cacheClassName = "net.sf.ehcache.Ehcache";
        if (ClassUtils.isPresent(cacheClassName, classLoader)) {
            defaultCacheInfoMapping.put(cacheClassName, EhcacheCacheInfo.class);
        }

        // Redis
        cacheClassName = "org.springframework.data.redis.core.StringRedisTemplate";
        if (ClassUtils.isPresent(cacheClassName, classLoader)) {
            defaultCacheInfoMapping.put(cacheClassName, RedisCacheInfo.class);
        }
    }

    
    private Map<String, Class<? extends ICacheInfo>> cacheInfoMapping;

    
    public Map<String, Class<? extends ICacheInfo>> getCacheInfoMapping() {
        return cacheInfoMapping;
    }

    
    public void setCacheInfoMapping(Map<String, Class<? extends ICacheInfo>> cacheInfoMapping) {
        this.cacheInfoMapping = cacheInfoMapping;
    }

    
    @Override
    public List<ICacheInfo> sListAndPrintCacheInfo() {
        return sListCacheInfo0(true);
    }

    
    @Override
    public List<ICacheInfo> sListCacheInfo() {
        return sListCacheInfo0(false);
    }

    private List<ICacheInfo> sListCacheInfo0(boolean print) {
        List<ICacheInfo> list = new ArrayList<ICacheInfo>();
        StringBuffer sb = new StringBuffer();
        for (Cache cache : getCaches()) {
            sb.append(EnvConsts.LINE_SEPARATOR).append("CacheName : ").append(cache.getName());
            ICacheInfo ci = buildCacheInfo(cache);
            if (null != ci) {
                list.add(ci);
                if (print) {
                    Set<Object> keys = ci.getKeys();
                    if (null == keys || keys.isEmpty()) {
                        sb.append(EnvConsts.LINE_SEPARATOR).append("  is empty. ");
                    } else {
                        sb.append(EnvConsts.LINE_SEPARATOR).append("  keys list: ");
                        int i = 0;
                        for (Object key : keys) {
                            sb.append(EnvConsts.LINE_SEPARATOR).append("    ").append(++i).append(". ").append(key);
                        }
                    }
                }
            } else if (print) {
                sb.append(EnvConsts.LINE_SEPARATOR).append("  not supported, the native class is ").append(cache.getNativeCache().getClass());
            }
        }
        if (print) {
            CommonLogger.info(sb.toString());
        }
        return list;
    }

    
    @Override
    public ICacheInfo sFindCacheInfo(String cacheName) {
        Cache cache = getCache(cacheName);
        return null == cache ? null : buildCacheInfo(cache);
    }

    
    @Override
    public void clear(String cacheName) {
        Caches.assertNotPumpCache(cacheName);
        Caches.clearCacheAndContainer(BaseConfig.getCacheManager(), cacheName);
    }

    
    @Override
    public void clearAll() {
        CacheManager cm = BaseConfig.getCacheManager();
        if (null != cm) {
            Collection<String> names = cm.getCacheNames();
            if (null != names) {
                for (String name : names) {
                    if (!Caches.isPumpCache(name)) {
                        Caches.clearCacheAndContainer(cm, name);
                    }
                }
            }
        }
    }

    
    @Override
    public void remove(String cacheName, Object key) {
        Caches.assertNotPumpCache(cacheName);
        Cache cache = getCache(cacheName);
        if (null != cache) {
            cache.evict(key);
        }
    }

    
    @Override
    public void remove(String cacheName, Object[] keys) {
        Caches.assertNotPumpCache(cacheName);
        Cache cache = getCache(cacheName);
        if (null != cache) {
            for (Object key : keys) {
                cache.evict(key);
            }
        }
    }

    
    private Cache getCache(String cacheName) {
        CacheManager cm = BaseConfig.getCacheManager();
        return null == cm ? null : cm.getCache(cacheName);
    }

    
    private Set<Cache> getCaches() {
        Set<Cache> caches = new LinkedHashSet<Cache>();
        CacheManager cm = BaseConfig.getCacheManager();
        if (null != cm) {
            Collection<String> names = cm.getCacheNames();
            if (null != names) {
                for (String name : names) {
                    caches.add(cm.getCache(name));
                }
            }
        }
        return caches;
    }

    
    private ICacheInfo buildCacheInfo(Cache cache) {
        String cacheClassName = cache.getNativeCache().getClass().getName();
        initConstructor(cacheClassName);
        Constructor<? extends ICacheInfo> constructor = constructors.get(cacheClassName);
        if (null != constructor) {
            try {
                return constructor.newInstance(cache);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    
    private void initConstructor(String cacheClassName) {
        if (!constructors.containsKey(cacheClassName)) {
            Class<? extends ICacheInfo> cls = null;
            Map<String, Class<? extends ICacheInfo>> cacheInfoMapping = getCacheInfoMapping();
            if (null != cacheInfoMapping) {
                cls = cacheInfoMapping.get(cacheClassName);
            }
            if (null == cls) {
                cls = defaultCacheInfoMapping.get(cacheClassName);
            } else {
                cacheInfoMapping.remove(cacheClassName);// 移除已经创建构造器的映射
            }
            if (null != cls) {
                Constructor<? extends ICacheInfo> constructor;
                try {
                    constructor = cls.getConstructor(Cache.class);
                    constructors.put(cacheClassName, constructor);
                } catch (Exception e) {
                    constructors.put(cacheClassName, null);
                    e.printStackTrace();
                }
            } else {
                constructors.put(cacheClassName, null);
            }
        }
    }
}
