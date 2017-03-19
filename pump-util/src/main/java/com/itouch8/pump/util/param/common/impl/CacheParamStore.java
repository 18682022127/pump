package com.itouch8.pump.util.param.common.impl;

import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;

import com.itouch8.pump.core.util.cache.Caches;
import com.itouch8.pump.util.param.IParam;
import com.itouch8.pump.util.param.common.IParamStore;


public class CacheParamStore<P extends IParam> implements IParamStore<P> {

    private final String storeName;

    private Cache cache;

    public CacheParamStore(String storeName) {
        super();
        this.storeName = storeName;
    }

    @SuppressWarnings("unchecked")
    @Override
    public P get(String name) {
        if (null != cache) {
            ValueWrapper valueWrapper = cache.get(name);
            if (null != valueWrapper) {
                return (P) valueWrapper.get();
            }
        }
        return null;
    }

    @Override
    public void remove(String name) {
        if (null != cache) {
            cache.evict(name);
        }
    }

    @Override
    public void save(String name, P value) {
        if (null == cache) {
            synchronized (CacheParamStore.class) {
                if (null == cache) {
                    Cache c = Caches.getCache(CacheParamStore.class, storeName);
                    cache = c;
                }
            }
        }
        cache.put(name, value);
    }

    @Override
    public boolean contains(String name) {
        return null != cache && null != cache.get(name);
    }

    @Override
    public void clear() {
        if (null != cache) {
            cache.clear();
        }
    }
}
