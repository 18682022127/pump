package com.itouch8.pump.core.util.cache.interceptor;


public interface ICacheInterceptor {

    
    public void onBeforeCacheOperations(ICacheMethod cacheMethod);

    
    public void onAfterCacheOperations(ICacheMethod cacheMethod);
}
