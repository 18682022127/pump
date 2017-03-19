package com.itouch8.pump.core.dao.sql.resolver;

import com.itouch8.pump.core.dao.jndi.IJndi;


public interface ISqlResolver {

    
    public boolean isSupport(IJndi jndi, String expression);

    
    public Object resolver(IJndi jndi, Object parameterObject, String expression);

    
    public String resolverSql(IJndi jndi, Object parameterObject, String expression);
}
