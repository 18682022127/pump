package com.itouch8.pump.core.util.cache.stat;

import java.util.Map;
import java.util.Set;

import com.itouch8.pump.core.util.annotation.Beta;


public interface ICacheInfo {

    
    String getCacheName();

    
    String getCacheType();

    
    @Beta
    boolean isDynamic();

    
    @Beta
    int getCapacity();

    
    @Beta
    int getSize();

    
    @Beta
    int getVisitNum();

    
    @Beta
    int getHitNum();

    
    Set<Object> getKeys();

    
    @Beta
    Map<String, Object> getNativeProperties();

    
    String getDes();
}
