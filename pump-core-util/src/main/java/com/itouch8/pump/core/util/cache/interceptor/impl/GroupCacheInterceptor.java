package com.itouch8.pump.core.util.cache.interceptor.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.springframework.cache.interceptor.CacheOperation;

import com.itouch8.pump.core.util.cache.interceptor.ICacheMethod;
import com.itouch8.pump.core.util.exception.Throw;
import com.itouch8.pump.core.util.exception.meta.ExceptionCodes;


public class GroupCacheInterceptor extends CacheInterceptorSupport {

    private Map<Pattern, Pattern> cacheNamePatternMapping;

    @Override
    public void onBeforeCacheOperations(ICacheMethod cacheMethod) {
        Set<String> cacheNames = getCacheNames(cacheMethod);
        for (String cacheName : cacheNames) {
            checkVisitCacheRight(cacheMethod, cacheName);
        }
    }

    
    private void checkVisitCacheRight(ICacheMethod cacheMethod, String cacheName) {
        String targetClassName = cacheMethod.getTargetClass().getName();
        Map<Pattern, Pattern> cacheNamePatternMapping = getCacheNamePatternMapping();
        if (null != cacheNamePatternMapping) {
            for (Pattern name : cacheNamePatternMapping.keySet()) {
                if (name.matcher(cacheName).find()) {
                    Pattern allow = cacheNamePatternMapping.get(name);
                    if (!allow.matcher(targetClassName).find()) {
                        Throw.throwRuntimeException(ExceptionCodes.YT010006, targetClassName, cacheName);
                    }
                }
            }
        }
    }

    private Set<String> getCacheNames(ICacheMethod cacheMethod) {
        Collection<CacheOperation> operations = cacheMethod.getOperations();
        Set<String> cacheNames = new HashSet<String>();
        for (CacheOperation operation : operations) {
            cacheNames.addAll(operation.getCacheNames());
        }
        return cacheNames;
    }

    public Map<Pattern, Pattern> getCacheNamePatternMapping() {
        return cacheNamePatternMapping;
    }

    public void setCacheNamePatternMapping(Map<Pattern, Pattern> cacheNamePatternMapping) {
        this.cacheNamePatternMapping = cacheNamePatternMapping;
    }
}
