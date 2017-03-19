package com.itouch8.pump.core.util.cache.stat.concurrent;

import java.util.LinkedHashSet;
import java.util.concurrent.ConcurrentMap;

import org.springframework.cache.Cache;

import com.itouch8.pump.core.util.cache.stat.base.CacheInfoSupport;


public class ConcurrentCacheInfo extends CacheInfoSupport {

    @SuppressWarnings("unchecked")
    public ConcurrentCacheInfo(Cache cache) {
        ConcurrentMap<Object, Object> nc = (ConcurrentMap<Object, Object>) cache.getNativeCache();
        this.cacheName = cache.getName();
        this.cacheType = "ConcurrentMap";
        this.keys = new LinkedHashSet<Object>(nc.keySet());
        this.size = this.keys.size();
        this.capacity = -1;
        this.dynamic = true;
    }
}
