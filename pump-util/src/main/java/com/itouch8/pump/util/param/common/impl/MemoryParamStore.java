package com.itouch8.pump.util.param.common.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.itouch8.pump.util.param.IParam;
import com.itouch8.pump.util.param.common.IParamStore;


public class MemoryParamStore<P extends IParam> implements IParamStore<P> {

    private final Map<String, P> cache = new ConcurrentHashMap<String, P>();

    @Override
    public P get(String name) {
        return cache.get(name);
    }

    @Override
    public void remove(String name) {
        cache.remove(name);
    }

    @Override
    public void save(String name, P value) {
        cache.put(name, value);
    }

    @Override
    public boolean contains(String name) {
        return cache.containsKey(name);
    }

    @Override
    public void clear() {
        cache.clear();
    }
}
