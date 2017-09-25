package com.itouch8.pump.core.dao.mybatis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.mybatis.spring.SqlSessionTemplate;

import com.itouch8.pump.core.dao.IDaoTemplate;
import com.itouch8.pump.core.dao.exception.DaoExceptionCodes;
import com.itouch8.pump.core.dao.sql.SqlManager;
import com.itouch8.pump.core.util.exception.Throw;


public class MybatisUtils {

    
    private static final List<DaoTemplateMapper> mappers = new ArrayList<DaoTemplateMapper>();

    
    private static final Map<String, DaoTemplateMapper> mapper = new HashMap<String, DaoTemplateMapper>();

    
     static void register(MybatisDaoTemplate daoTemplate) {
        DaoTemplateMapper m = new DaoTemplateMapper(daoTemplate);
        for (Object ms : m.configuration.getMappedStatements()) {// Mybatis返回的集合中存在Ambiguity对象
            if (ms instanceof MappedStatement) {
                mapper.put(((MappedStatement) ms).getId(), m);
            }
        }
        mappers.add(m);
    }

    
    public static DataSource getDataSource(String statement) {
        return getJndiSqlSessionTemplateMapper(statement).dataSource;
    }

    
    public static Configuration getConfiguration(String statement) {
        return getJndiSqlSessionTemplateMapper(statement).configuration;
    }

    
    public static SqlSessionTemplate getSqlSession(String statement) {
        return getJndiSqlSessionTemplateMapper(statement).sqlSessionTemplate;
    }

    
    public static SqlSessionTemplate getBatchSqlSession(String statement) {
        return getJndiSqlSessionTemplateMapper(statement).batchSqlSessionTemplate;
    }

    
    public static MappedStatement getMappedStatement(String statement) {
        return getJndiSqlSessionTemplateMapper(statement).configuration.getMappedStatement(statement);
    }

    
    public static IDaoTemplate getDaoTemplate(String statement) {
        return getJndiSqlSessionTemplateMapper(statement).dao;
    }

    
    public static void openBatchType() {
        for (DaoTemplateMapper mapper : mappers) {
            mapper.dao.openBatchType();
        }
    }

    
    public static void resetExecutorType() {
        for (DaoTemplateMapper mapper : mappers) {
            mapper.dao.resetExecutorType();
        }
    }

    
    private static DaoTemplateMapper getJndiSqlSessionTemplateMapper(String statement) {
        statement = SqlManager.getExecuteSqlId(statement);
        DaoTemplateMapper jdtm = mapper.get(statement);
        if (null == jdtm) {
            Throw.throwRuntimeException(DaoExceptionCodes.YT020016, statement);
        }
        return jdtm;
    }

    
    private static class DaoTemplateMapper {
        private final IDaoTemplate dao;
        private final DataSource dataSource;
        private final SqlSessionTemplate sqlSessionTemplate;
        private final SqlSessionTemplate batchSqlSessionTemplate;
        private final Configuration configuration;

        private DaoTemplateMapper(MybatisDaoTemplate daoTemplate) {
            this.dao = daoTemplate;
            this.sqlSessionTemplate = daoTemplate.getSqlSession();
            this.batchSqlSessionTemplate = daoTemplate.getBatchSqlSession();
            this.configuration = sqlSessionTemplate.getConfiguration();
            this.dataSource = this.configuration.getEnvironment().getDataSource();
        }
    }
}
