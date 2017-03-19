package com.itouch8.pump.core.dao.sql.mapper.impl;

import java.lang.reflect.Method;

import com.itouch8.pump.core.dao.annotation.SqlRef;
import com.itouch8.pump.core.dao.sql.SqlManager;
import com.itouch8.pump.core.dao.sql.mapper.ISqlMapperStrategy;


public class SqlRefClassNameMapperStrategy implements ISqlMapperStrategy {

    
    public String lookup(Method method) {
        String rs = "";
        SqlRef sqlRef = method.getAnnotation(SqlRef.class);
        if (null != sqlRef) {
            rs = SqlManager.resolverSqlId(sqlRef, method);
        } else {
            rs = method.getDeclaringClass().getName() + "." + method.getName();
        }
        return SqlManager.getExecuteSqlId(rs);
    }

}
