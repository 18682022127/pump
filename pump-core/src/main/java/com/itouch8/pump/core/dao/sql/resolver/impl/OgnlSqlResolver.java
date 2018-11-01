package com.itouch8.pump.core.dao.sql.resolver.impl;

import org.apache.ibatis.ognl.Ognl;
import org.apache.ibatis.ognl.OgnlException;

import com.itouch8.pump.core.dao.jndi.IJndi;
import com.itouch8.pump.core.util.exception.Throw;


public class OgnlSqlResolver extends AbstractSqlResolver {

    
    @Override
    public boolean isSupport(IJndi jndi, String expression) {
        return null != expression && expression.startsWith("%");
    }

    
    @Override
    public Object resolver(IJndi jndi, Object parameterObject, String expression) {
        String exp = expression.substring(1);
        try {
            return Ognl.getValue(exp, parameterObject);
        } catch (OgnlException e) {
            throw Throw.createRuntimeException("pump.core.dao.ognl_resolver_db_params", e, exp);
        }
    }

}
