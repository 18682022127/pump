package com.itouch8.pump.core.dao.mybatis.executor;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.transaction.Transaction;

import com.itouch8.pump.core.dao.jndi.IJndi;

public class ReuseExecutor extends org.apache.ibatis.executor.ReuseExecutor implements IJndiExecutor {

    private IJndi jndi;

    public ReuseExecutor(Configuration configuration, Transaction transaction, IJndi jndi) {
        super(configuration, transaction);
        this.jndi = jndi;
    }

    public IJndi getJndi() {
        return jndi;
    }

    @Override
    public CacheKey createCacheKey(MappedStatement ms, Object parameterObject, RowBounds rowBounds, BoundSql boundSql) {
        return CacheKeyHelp.createCacheKey(this, this.configuration, ms, parameterObject, rowBounds, boundSql);
    }
}
