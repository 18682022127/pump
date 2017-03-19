package com.itouch8.pump.core.util.cache.stat;

import java.util.List;


public interface ICacheStatService {

    
    public List<ICacheInfo> sListAndPrintCacheInfo();

    
    public List<ICacheInfo> sListCacheInfo();

    
    public ICacheInfo sFindCacheInfo(String cacheName);

    
    public void clear(String cacheName);

    
    public void clearAll();

    
    public void remove(String cacheName, Object key);

    
    public void remove(String cacheName, Object[] keys);
}
