package com.itouch8.pump.core.dao.sql.function.impl;

import org.apache.ibatis.ognl.Ognl;
import org.apache.ibatis.ognl.OgnlException;

import com.itouch8.pump.core.dao.dialect.IDialect.DBType;
import com.itouch8.pump.core.dao.jndi.IJndi;
import com.itouch8.pump.core.util.CoreUtils;
import com.itouch8.pump.core.util.exception.Throw;


public class ConcatSqlConfigFunction extends AbstractSqlConfigFunction {

    @Override
    public String getName() {
        return "concat";
    }

    @Override
    public Object evaluateValue(IJndi jndi, Object parameter, String expression, String[] args) {
        try {
            return Ognl.getValue(CoreUtils.join(args, "+"), parameter);
        } catch (OgnlException e) {
            e.printStackTrace();
            throw Throw.createRuntimeException(e);
        }
    }

    @Override
    public String evaluateSql(IJndi jndi, Object parameter, String expression, String[] args) {
        DBType type = jndi.getDialect().getType();
        if (DBType.MySql.equals(type)) {
            return "CONCAT(" + CoreUtils.join(args, ",") + ")";
        } else {
            return CoreUtils.join(args, "||");
        }
    }
}
