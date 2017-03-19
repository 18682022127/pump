package com.itouch8.pump.core.dao.mybatis.executor;

import java.util.List;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;

import com.itouch8.pump.core.PumpConfig;
import com.itouch8.pump.core.dao.jndi.IJndi;
import com.itouch8.pump.core.dao.sql.resolver.ISqlResolver;

public class CacheKeyHelp {

    public static CacheKey createCacheKey(IJndiExecutor executor, Configuration configuration, MappedStatement ms, Object parameterObject, RowBounds rowBounds, BoundSql boundSql) {
        if (executor.isClosed()) {
            throw new ExecutorException("Executor was closed.");
        }
        CacheKey cacheKey = new CacheKey();
        cacheKey.update(ms.getId());
        cacheKey.update(Integer.valueOf(rowBounds.getOffset()));
        cacheKey.update(Integer.valueOf(rowBounds.getLimit()));
        cacheKey.update(boundSql.getSql());
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        TypeHandlerRegistry typeHandlerRegistry = ms.getConfiguration().getTypeHandlerRegistry();
        // mimic DefaultParameterHandler logic

        IJndi jndi = executor.getJndi();
        MetaObject metaObject = parameterObject == null ? null : configuration.newMetaObject(parameterObject);
        for (int i = 0; i < parameterMappings.size(); i++) {
            ParameterMapping parameterMapping = parameterMappings.get(i);
            if (parameterMapping.getMode() != ParameterMode.OUT) {
                Object value;
                String propertyName = parameterMapping.getProperty();
                value = resolverExpressionValue(jndi, boundSql, parameterObject, typeHandlerRegistry, metaObject, propertyName);
                cacheKey.update(value);
            }
        }
        if (configuration.getEnvironment() != null) {
            // issue #176
            cacheKey.update(configuration.getEnvironment().getId());
        }
        return cacheKey;
    }

    public static Object resolverExpressionValue(IJndi jndi, BoundSql boundSql, Object parameterObject, TypeHandlerRegistry typeHandlerRegistry, MetaObject metaObject, String propertyName) {
        Object value;
        ISqlResolver spr = getSqlResolver(jndi, propertyName);
        if (null != spr) {
            value = spr.resolver(jndi, parameterObject, propertyName);
        } else if (boundSql.hasAdditionalParameter(propertyName)) { // issue #448 ask first for
                                                                    // additional params
            value = boundSql.getAdditionalParameter(propertyName);
        } else if (parameterObject == null) {
            value = null;
        } else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
            value = parameterObject;
        } else {
            value = metaObject == null ? null : metaObject.getValue(propertyName);
        }
        return value;
    }

    private static ISqlResolver getSqlResolver(IJndi jndi, String propertyName) {
        List<? extends ISqlResolver> sprs = PumpConfig.getSqlResolvers();
        if (null != sprs && !sprs.isEmpty()) {
            for (ISqlResolver spr : sprs) {
                if (spr.isSupport(jndi, propertyName)) {
                    return spr;
                }
            }
        }
        return null;
    }
}
