package com.itouch8.pump.core.util.cache.errorhandler;

import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.CacheErrorHandler;

import com.itouch8.pump.ReturnCodes;
import com.itouch8.pump.core.util.exception.Throw;


public class PumpCacheErrorHandler implements CacheErrorHandler {

    
    private boolean ignoreCacheError;

    
    @Override
    public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
        if (isIgnoreCacheError() && !commonHandler(exception)) {
            Throw.throwRuntimeException(ReturnCodes.SYSTEM_ERROR.code, exception, "get", cache.getName());
        }
    }

    
    @Override
    public void handleCachePutError(RuntimeException exception, Cache cache, Object key, Object value) {
        if (isIgnoreCacheError() && !commonHandler(exception)) {
            Throw.throwRuntimeException(ReturnCodes.SYSTEM_ERROR.code, exception, "put", cache.getName());
        }
    }

    
    @Override
    public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
        if (isIgnoreCacheError() && !commonHandler(exception)) {
            Throw.throwRuntimeException(ReturnCodes.SYSTEM_ERROR.code, exception, "evict", cache.getName());
        }
    }

    
    @Override
    public void handleCacheClearError(RuntimeException exception, Cache cache) {
        if (isIgnoreCacheError() && !commonHandler(exception)) {
            Throw.throwRuntimeException(ReturnCodes.SYSTEM_ERROR.code, exception, "clear", cache.getName());
        }
    }

    
    public boolean isIgnoreCacheError() {
        return ignoreCacheError;
    }

    
    public void setIgnoreCacheError(boolean ignoreCacheError) {
        this.ignoreCacheError = ignoreCacheError;
    }

    
    protected boolean commonHandler(RuntimeException exception) {
        exception.printStackTrace();
        return false;
    }
}
