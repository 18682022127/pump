package com.itouch8.pump.core.util.cache.stat.base;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.itouch8.pump.core.util.cache.stat.ICacheInfo;


public abstract class CacheInfoSupport implements ICacheInfo {

    protected String cacheName; // 缓存名称

    protected String cacheType; // 缓存类型

    protected boolean dynamic;// 是否动态扩展缓存容器

    protected int capacity;// 缓存容器大小

    protected int size;// 已缓存条目数

    protected int visitNum;// 访问次数

    protected int hitNum;// 命中次数

    protected Set<Object> keys;// 缓存键值

    protected Map<String, Object> nativeProperties = new LinkedHashMap<String, Object>();// 缓存实现相关的特征属性

    protected String des;// 描述

    @Override
    public String getCacheName() {
        return cacheName;
    }

    @Override
    public String getCacheType() {
        return cacheType;
    }

    @Override
    public boolean isDynamic() {
        return dynamic;
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public int getVisitNum() {
        return visitNum;
    }

    @Override
    public int getHitNum() {
        return hitNum;
    }

    @Override
    public Set<Object> getKeys() {
        return keys;
    }

    @Override
    public Map<String, Object> getNativeProperties() {
        return nativeProperties;
    }

    @Override
    public String getDes() {
        return des;
    }
}
