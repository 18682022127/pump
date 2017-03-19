package com.itouch8.pump.core.dao.mybatis.parameter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;

import com.itouch8.pump.core.dao.jndi.IJndi;
import com.itouch8.pump.core.dao.jndi.JndiManager;
import com.itouch8.pump.core.dao.mybatis.MybatisUtils;
import com.itouch8.pump.core.dao.mybatis.executor.CacheKeyHelp;


public class ExpressionParameterHandler implements ParameterHandler {

    private TypeHandlerRegistry typeHandlerRegistry;
    private MappedStatement mappedStatement;
    private Object parameterObject;
    private BoundSql boundSql;
    private Configuration configuration;
    private IJndi jndi;

    public ExpressionParameterHandler() {}

    public ExpressionParameterHandler(MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql) {
        this.mappedStatement = mappedStatement;
        this.configuration = mappedStatement.getConfiguration();
        this.typeHandlerRegistry = mappedStatement.getConfiguration().getTypeHandlerRegistry();
        this.parameterObject = parameterObject;
        this.boundSql = boundSql;
        this.jndi = JndiManager.getJndi(MybatisUtils.getDataSource(mappedStatement.getId()));
    }

    public Object getParameterObject() {
        return parameterObject;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public MappedStatement getMappedStatement() {
        return mappedStatement;
    }

    public BoundSql getBoundSql() {
        return boundSql;
    }

    public TypeHandlerRegistry getTypeHandlerRegistry() {
        return typeHandlerRegistry;
    }

    public void setParameters(PreparedStatement ps) throws SQLException {
        ErrorContext.instance().activity("setting parameters").object(mappedStatement.getParameterMap().getId());
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        if (parameterMappings != null) {
            MetaObject metaObject = parameterObject == null ? null : configuration.newMetaObject(parameterObject);
            for (int i = 0; i < parameterMappings.size(); i++) {
                ParameterMapping parameterMapping = parameterMappings.get(i);
                if (parameterMapping.getMode() != ParameterMode.OUT) {
                    setParameter(metaObject, parameterMapping, ps, i + 1);
                }
            }
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    protected void setParameter(MetaObject metaObject, ParameterMapping parameterMapping, PreparedStatement ps, int parameterIndex) throws SQLException {
        String propertyName = parameterMapping.getProperty();
        Object value = CacheKeyHelp.resolverExpressionValue(jndi, boundSql, parameterObject, typeHandlerRegistry, metaObject, propertyName);
        JdbcType jdbcType = parameterMapping.getJdbcType();
        if (value == null && jdbcType == null) {
            jdbcType = configuration.getJdbcTypeForNull();
        }
        TypeHandler typeHandler = parameterMapping.getTypeHandler();
        typeHandler.setParameter(ps, parameterIndex, value, jdbcType);
    }
}
