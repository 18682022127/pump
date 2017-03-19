package com.itouch8.pump.core.dao.sql.function.impl;

import com.itouch8.pump.core.dao.sql.function.ISqlConfigFunction;
import com.itouch8.pump.core.dao.sql.function.ISqlConfigFunctionFactory;


public abstract class AbstractSingleonSqlConfigFunctionFactory implements ISqlConfigFunctionFactory {

    @Override
    public ISqlConfigFunction getSqlConfigFunction(String name) {
        return null;
    }

}
