package com.itouch8.pump.core.dao.mybatis.parse;

import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.scripting.xmltags.XMLLanguageDriver;

import com.itouch8.pump.core.dao.mybatis.parameter.ExpressionParameterHandler;

public class EXMLLanguageDriver extends XMLLanguageDriver {

    @Override
    public ParameterHandler createParameterHandler(MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql) {
        return new ExpressionParameterHandler(mappedStatement, parameterObject, boundSql);
    }
}
