package com.itouch8.pump.core.dao.sql.resolver.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.itouch8.pump.core.dao.jndi.IJndi;
import com.itouch8.pump.core.dao.sql.function.ISqlConfigFunction;
import com.itouch8.pump.core.dao.sql.resolver.ISqlResolver;


public class SqlConfigFunctionSqlResolver implements ISqlResolver {

    private static final Pattern pattern = Pattern.compile("^\\s*(\\w+)\\s*\\(\\s*(.*)\\s*\\)\\s*$");

    
    @Override
    public boolean isSupport(IJndi jndi, String expression) {
        return null != expression && pattern.matcher(expression).find();
    }

    
    @Override
    public Object resolver(IJndi jndi, Object parameterObject, String expression) {
        Matcher matcher = pattern.matcher(expression);
        if (matcher.find()) {
            String fname = matcher.group(1);
            String args = matcher.group(2);
            ISqlConfigFunction fn = ISqlConfigFunction.Manager.getSqlTextFunction(fname);
            if (null != fn) {
                return fn.evaluateValue(jndi, parameterObject, args, args == null ? null : args.split("\\s+|(\\s*,\\s*)"));
            }
        }
        return expression;
    }

    @Override
    public String resolverSql(IJndi jndi, Object parameterObject, String expression) {
        Matcher matcher = pattern.matcher(expression);
        if (matcher.find()) {
            String fname = matcher.group(1);
            String args = matcher.group(2);
            ISqlConfigFunction fn = ISqlConfigFunction.Manager.getSqlTextFunction(fname);
            if (null != fn) {
                return fn.evaluateSql(jndi, parameterObject, args, args == null ? null : args.split("\\s+|(\\s*,\\s*)"));
            }
        }
        return expression;
    }
}
