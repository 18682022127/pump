package com.itouch8.pump.core.service.mapping.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.beans.factory.InitializingBean;

import com.itouch8.pump.core.service.mapping.IMapping;
import com.itouch8.pump.core.util.exception.Throw;
import com.itouch8.pump.core.util.logger.CommonLogger;


public abstract class AbstractMapping<K, V> implements IMapping<K, V>, InitializingBean {

    
    private boolean lazyInit = true;

    
    private boolean allowDuplicate = true;

    
    private AtomicBoolean monitor = new AtomicBoolean(false);

    
    private Map<K, V> mapping = new HashMap<K, V>();

    @Override
    public V get(K key) {
        sureInited();
        Map<K, V> mapping = getMapping();
        return mapping.get(key);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (isLazyInit()) {
            return;
        }
        init();
    }

    protected void init() {
        Set<V> set = initializeMapping();
        if (null == set || set.isEmpty()) {
            return;
        }
        Map<K, V> mapping = getMapping();
        for (V s : set) {
            K key = getKey(s);
            if (key == null) {
                continue;
            }
            if (mapping.containsKey(key)) {
                V o2 = mapping.get(key);
                if (isAllowDuplicate()) {
                    if (getOrder(o2) > getOrder(s)) {
                        mapping.put(key, s);
                        CommonLogger.debug("mapping key [" + key + "] is duplicate, according to the order, use the map [" + s + "]");
                    } else {
                        CommonLogger.debug("mapping key [" + key + "] is duplicate, according to the order, ignore the map [" + s + "]");
                        continue;
                    }
                } else {
                    Throw.createRuntimeException("mapping key [" + key + "] is duplicate,mapping:[" + o2 + "," + s + "]");
                }
            } else {
                mapping.put(key, s);
                CommonLogger.debug("mapping key [" + key + "] is [" + s + "]");
            }
        }
    }

    
    protected abstract Set<V> initializeMapping();

    
    protected abstract K getKey(V value);

    
    protected abstract int getOrder(V value);

    private Map<K, V> getMapping() {
        return mapping;
    }

    private void sureInited() {
        if (!isLazyInit()) {
            return;
        }
        if (!monitor.get()) {
            synchronized (monitor) {
                if (!monitor.get()) {
                    try {
                        init();
                    } finally {
                        monitor.set(true);
                    }
                }
            }
        }
    }

    public boolean isAllowDuplicate() {
        return allowDuplicate;
    }

    public void setAllowDuplicate(boolean allowDuplicate) {
        this.allowDuplicate = allowDuplicate;
    }

    public boolean isLazyInit() {
        return lazyInit;
    }

    public void setLazyInit(boolean lazyInit) {
        this.lazyInit = lazyInit;
    }
}
