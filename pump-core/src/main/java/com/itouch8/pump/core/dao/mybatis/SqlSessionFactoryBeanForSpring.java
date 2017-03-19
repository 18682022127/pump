package com.itouch8.pump.core.dao.mybatis;

import static org.springframework.util.StringUtils.hasLength;
import static org.springframework.util.StringUtils.tokenizeToStringArray;

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
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.context.ConfigurableApplicationContext;

import com.itouch8.pump.core.dao.mybatis.mapper.MapperRegistry;
import com.itouch8.pump.core.util.CoreUtils;
import com.itouch8.pump.core.util.config.BaseConfig;
import com.itouch8.pump.core.util.logger.CommonLogger;


public class SqlSessionFactoryBeanForSpring extends SqlSessionFactoryBean {

    private boolean autoScanTypeAliases = false;

    private Class<?> baseClass;

    private String typeAliasesScanPackage = BaseConfig.getScanPackage();

    public void setTypeAliasesScanPackage(String typeAliasesScanPackage) {
        this.typeAliasesScanPackage = typeAliasesScanPackage;
    }

    public void setAutoScanTypeAliases(boolean autoScanTypeAliases) {
        this.autoScanTypeAliases = autoScanTypeAliases;
    }

    public void setBaseClass(Class<?> baseClass) {
        this.baseClass = baseClass;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        SqlSessionFactory sqlSessionFactory = getObject();
        MybatisUtils.register(new MybatisDaoTemplate(sqlSessionFactory));
    }

    
    protected SqlSessionFactory buildSqlSessionFactory() throws IOException {
        scanTypeAliases();
        SqlSessionFactory sqlSessionFactory = super.buildSqlSessionFactory();
        Configuration config = sqlSessionFactory.getConfiguration();
        if (config instanceof com.itouch8.pump.core.dao.mybatis.Configuration) {
            config = ((com.itouch8.pump.core.dao.mybatis.Configuration) config).getDelegate();
        }
        setDefaultResultType(config, Map.class);
        CoreUtils.setProperty(config, "mapperRegistry", new MapperRegistry(config));
        return sqlSessionFactory;
    }

    
    private void scanTypeAliases() {
        if (this.autoScanTypeAliases && hasLength(this.typeAliasesScanPackage) && null != baseClass) {
            String[] typeAliasPackageArray = tokenizeToStringArray(this.typeAliasesScanPackage, ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);
            List<Class<?>> list = new ArrayList<Class<?>>();
            List<String> alias = new ArrayList<String>();
            MetaObject meta = SystemMetaObject.forObject(this);
            Class<?>[] orig = (Class<?>[]) meta.getValue("typeAliases");
            if (null != orig) {
                for (Class<?> t : orig) {
                    list.add(t);
                    alias.add(t.getSimpleName().toLowerCase());
                }
            }
            for (String packageToScan : typeAliasPackageArray) {
                for (Class<?> type : CoreUtils.scanClassesByParentCls(packageToScan, baseClass)) {
                    String a = type.getSimpleName().toLowerCase();
                    if (!alias.contains(a)) {
                        list.add(type);
                        alias.add(a);
                    } else {
                        CommonLogger.warn("Mybatis在自动扫描注册别名时，发现有多个可简写为" + type.getSimpleName() + "的类，将取第一个类，忽略" + type);
                    }
                }
            }
            super.setTypeAliases(list.toArray(new Class<?>[list.size()]));
        }
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
