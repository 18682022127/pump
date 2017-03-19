package com.itouch8.pump.core.dao.sql.resolver.impl;

import com.itouch8.pump.core.dao.jndi.IJndi;
import com.itouch8.pump.core.service.spring.SpringHelp;


public class SpelSqlResolver extends AbstractSqlResolver {

    
    @Override
    public boolean isSupport(IJndi jndi, String expression) {
        return null != expression && expression.startsWith("@");
    }

    
    @Override
    public Object resolver(IJndi jndi, Object parameterObject, String expression) {
        String exp = expression.substring(1);
        return SpringHelp.evaluate(parameterObject, exp);
    }

}
