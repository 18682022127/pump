package com.itouch8.pump.core.dao.sql.function.impl;

import com.itouch8.pump.core.dao.jndi.IJndi;


public abstract class AbstractValueSqlConfigFunction extends AbstractSqlConfigFunction {

    @Override
    public String evaluateSql(IJndi jndi, Object parameter, String expression, String[] args) {
        Object value = this.evaluateValue(jndi, parameter, expression, args);
        if (value instanceof Number) {
            return value.toString();
        } else {
            return "'" + value.toString() + "'";
        }
    }

}
