package com.itouch8.pump.core.dao.sql.resolver.impl;

import com.itouch8.pump.core.dao.jndi.IJndi;
import com.itouch8.pump.core.dao.sql.resolver.ISqlResolver;


public abstract class AbstractSqlResolver implements ISqlResolver {

    @Override
    public String resolverSql(IJndi jndi, Object parameterObject, String expression) {
        Object value = this.resolver(jndi, parameterObject, expression);
        return null == value ? expression : value.toString();
    }
}
