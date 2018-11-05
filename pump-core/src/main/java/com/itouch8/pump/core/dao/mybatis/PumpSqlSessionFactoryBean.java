package com.itouch8.pump.core.dao.mybatis;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;

public class PumpSqlSessionFactoryBean extends SqlSessionFactoryBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        SqlSessionFactory sqlSessionFactory = getObject();
        MybatisUtils.register(new MybatisDaoTemplate(sqlSessionFactory));
    }
    
    protected SqlSessionFactory buildSqlSessionFactory() throws IOException {
        SqlSessionFactory sqlSessionFactory = super.buildSqlSessionFactory();
        Configuration config = sqlSessionFactory.getConfiguration();
        setDefaultResultType(config, Map.class);
        return sqlSessionFactory;
    }

    private void setDefaultResultType(Configuration configuration, Class<?> cls) {
        try {
            Field resultMaps = MappedStatement.class.getDeclaredField("resultMaps");
            resultMaps.setAccessible(true);
            for (Iterator<MappedStatement> i = configuration.getMappedStatements().iterator(); i.hasNext();) {
                Object o = i.next();
                if (o instanceof MappedStatement) {
                    MappedStatement ms = (MappedStatement) o;
                    if (SqlCommandType.SELECT.equals(ms.getSqlCommandType()) && ms.getResultMaps().isEmpty()) {
                        ResultMap.Builder inlineResultMapBuilder = new ResultMap.Builder(configuration, ms.getId() + "-Inline", cls, new ArrayList<ResultMapping>(), null);
                        ResultMap resultMap = inlineResultMapBuilder.build();
                        List<ResultMap> rm = new ArrayList<ResultMap>();
                        rm.add(resultMap);
                        resultMaps.set(ms, Collections.unmodifiableList(rm));
                    } else {
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
