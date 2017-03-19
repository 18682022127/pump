package com.itouch8.pump.core.dao.sql.mapper;

import java.lang.reflect.Method;


public interface ISqlMapperStrategy {

    
    public String lookup(Method method);
}
