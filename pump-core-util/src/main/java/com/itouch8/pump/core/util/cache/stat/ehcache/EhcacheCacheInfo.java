package com.itouch8.pump.core.util.cache.stat.ehcache;

import java.util.LinkedHashSet;

import org.springframework.cache.Cache;

import com.itouch8.pump.core.util.cache.stat.base.CacheInfoSupport;

import net.sf.ehcache.Ehcache;


public class EhcacheCacheInfo extends CacheInfoSupport {

    @SuppressWarnings("unchecked")
    public EhcacheCacheInfo(Cache cache) {
        Ehcache ec = (Ehcache) cache.getNativeCache();
        this.cacheName = cache.getName();
        this.cacheType = "ConcurrentMap";
        this.keys = new LinkedHashSet<Object>(ec.getKeys());
        this.size = this.keys.size();
    }
}
