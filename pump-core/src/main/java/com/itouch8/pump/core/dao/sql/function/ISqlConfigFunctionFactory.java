package com.itouch8.pump.core.dao.sql.function;

import java.util.Set;


public interface ISqlConfigFunctionFactory {

    
    public Set<ISqlConfigFunction> getAllSqlConfigFunctions();

    
    public ISqlConfigFunction getSqlConfigFunction(String name);
}
