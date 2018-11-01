package com.itouch8.pump.core;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.itouch8.pump.core.dao.dialect.IDialect;
import com.itouch8.pump.core.dao.dialect.impl.Db2;
import com.itouch8.pump.core.dao.dialect.impl.H2;
import com.itouch8.pump.core.dao.dialect.impl.MySQL;
import com.itouch8.pump.core.dao.dialect.impl.Oracle;
import com.itouch8.pump.core.dao.dialect.impl.SybaseASE;
import com.itouch8.pump.core.dao.dialect.impl.SybaseIQ;
import com.itouch8.pump.core.dao.mybatis.mapper.IMapperMethodExecutor;
import com.itouch8.pump.core.dao.sql.interceptor.ISqlInterceptor;
import com.itouch8.pump.core.dao.sql.mapper.ISqlMapperStrategy;
import com.itouch8.pump.core.dao.sql.resolver.ISqlResolver;
import com.itouch8.pump.core.service.request.IRequestInfoFactory;
import com.itouch8.pump.core.util.annotation.Configable;
import com.itouch8.pump.core.util.aop.IAopInterceptor;
import com.itouch8.pump.core.util.config.BaseConfig;


public class PumpConfig extends BaseConfig {

    private static final LinkedHashMap<String, IDialect> defaultDatabaseProductNameDialectMapping = new LinkedHashMap<String, IDialect>();
    static {
        defaultDatabaseProductNameDialectMapping.put("oracle", Oracle.getSingleInstance());
        defaultDatabaseProductNameDialectMapping.put("mysql", MySQL.getSingleInstance());
        defaultDatabaseProductNameDialectMapping.put("db2", Db2.getSingleInstance());
        defaultDatabaseProductNameDialectMapping.put("h2", H2.getSingleInstance());
        defaultDatabaseProductNameDialectMapping.put("ADAPTIVE SERVER ENTERPRISE", SybaseASE.getSingleInstance());
        defaultDatabaseProductNameDialectMapping.put("IQ", SybaseIQ.getSingleInstance());
    }

    
    @Configable
    private static String pumpTablePrefix = "";

    
    @Configable
    private static String totalRecordsParamName;

    
    @Configable
    private static String pageSizeParamName;

    
    @Configable
    private static int defaultPageSize = EQUAL_EMPTY_INT_VALUE;

    
    @Configable
    private static String currentPageParamName;

    
    @Configable
    private static String devModeConfigName;

    
    @Configable
    private static String localeConfigName;

    
    @Configable
    private static String themeConfigName;

    
    @Configable
    private static String defaultTheme;

    
    @Configable
    private static Map<String, String> sqlIdMapping;

    
    @Configable
    private static List<ISqlInterceptor> sqlInterceptors;

    
    @Configable
    private static List<ISqlResolver> sqlResolvers;

    
    @Configable
    private static List<IMapperMethodExecutor> mapperMethodExecutors;

    
    @Configable
    private static LinkedHashMap<String, IDialect> databaseProductNameDialectMapping = defaultDatabaseProductNameDialectMapping;

    
    @Configable
    private static Map<Pattern, String> mybatisLogTypeMapping;

    
    @Configable
    private static ISqlMapperStrategy sqlMapperStrategy;

    
    @Configable
    private static IRequestInfoFactory requestInfoFactory;

    @Configable
    private static List<IAopInterceptor> serviceAopInterceptors;

    
    @Override
    public void validate() {
        super.validate();
        // 执行当前配置类中的校验
    }

    
    public static Map<String, String> getSqlIdMapping() {
        return sqlIdMapping;
    }

    
    public void setSqlIdMapping(Map<String, String> sqlIdMapping) {
        PumpConfig.sqlIdMapping = sqlIdMapping;
    }

    
    public static String getPumpTablePrefix() {
        return getValue(pumpTablePrefix, "table_prefix");
    }

    
    public void setPumpTablePrefix(String tablePrefix) {
        PumpConfig.pumpTablePrefix = tablePrefix;
    }

    public static String getTotalRecordsParamName() {
        return getValue(totalRecordsParamName, "totalRecordsParamName");
    }

    public void setTotalRecordsParamName(String totalRecordsParamName) {
        PumpConfig.totalRecordsParamName = totalRecordsParamName;
    }

    public static String getPageSizeParamName() {
        return getValue(pageSizeParamName, "pageSizeParamName");
    }

    public void setPageSizeParamName(String pageSizeParamName) {
        PumpConfig.pageSizeParamName = pageSizeParamName;
    }

    public static int getDefaultPageSize() {
        return getValue(defaultPageSize, "defaultPageSize");
    }

    public void setDefaultPageSize(int defaultPageSize) {
        PumpConfig.defaultPageSize = defaultPageSize;
    }

    public static String getCurrentPageParamName() {
        return getValue(currentPageParamName, "currentPageParamName");
    }

    public void setCurrentPageParamName(String currentPageParamName) {
        PumpConfig.currentPageParamName = currentPageParamName;
    }

    public static String getDevModeConfigName() {
        return getValue(devModeConfigName, "devModeConfigName");
    }

    public void setDevModeConfigName(String devModeConfigName) {
        PumpConfig.devModeConfigName = devModeConfigName;
    }

    public static String getLocaleConfigName() {
        return getValue(localeConfigName, "localeConfigName");
    }

    public void setLocaleConfigName(String localeConfigName) {
        PumpConfig.localeConfigName = localeConfigName;
    }

    public static String getThemeConfigName() {
        return getValue(themeConfigName, "themeConfigName");
    }

    public void setThemeConfigName(String themeConfigName) {
        PumpConfig.themeConfigName = themeConfigName;
    }

    public static String getDefaultTheme() {
        return getValue(defaultTheme, "defaultTheme");
    }

    public void setDefaultTheme(String defaultTheme) {
        PumpConfig.defaultTheme = defaultTheme;
    }

    
    public static List<ISqlInterceptor> getSqlInterceptors() {
        return getComponents(sqlInterceptors, ISqlInterceptor.class);
    }

    
    public void setSqlInterceptors(List<ISqlInterceptor> sqlInterceptors) {
        PumpConfig.sqlInterceptors = sqlInterceptors;
    }

    
    public static List<ISqlResolver> getSqlResolvers() {
        return getComponents(sqlResolvers, ISqlResolver.class);
    }

    
    public void setSqlResolvers(List<ISqlResolver> sqlResolvers) {
        PumpConfig.sqlResolvers = sqlResolvers;
    }

    
    public static List<IMapperMethodExecutor> getMapperMethodExecutors() {
        return mapperMethodExecutors;
    }

    
    public void setMapperMethodExecutors(List<IMapperMethodExecutor> mapperMethodExecutors) {
        PumpConfig.mapperMethodExecutors = mapperMethodExecutors;
    }

    
    public static Map<String, IDialect> getDatabaseProductNameDialectMapping() {
        return databaseProductNameDialectMapping;
    }

    
    public void setDatabaseProductNameDialectMapping(LinkedHashMap<String, IDialect> databaseProductNameDialectMapping) {
        if (null != databaseProductNameDialectMapping && !databaseProductNameDialectMapping.isEmpty()) {
            if (null != PumpConfig.databaseProductNameDialectMapping) {// 在前面插入应用配置
                databaseProductNameDialectMapping.putAll(PumpConfig.databaseProductNameDialectMapping);
            }
            PumpConfig.databaseProductNameDialectMapping = databaseProductNameDialectMapping;
        }
    }

    
    public static Map<Pattern, String> getMybatisLogTypeMapping() {
        return mybatisLogTypeMapping;
    }

    
    public void setMybatisLogTypeMapping(Map<Pattern, String> mybatisLogTypeMapping) {
        PumpConfig.mybatisLogTypeMapping = mybatisLogTypeMapping;
    }

    
    public static ISqlMapperStrategy getSqlMapperStrategy() {
        return getComponent(sqlMapperStrategy, ISqlMapperStrategy.class);
    }

    
    public void setSqlMapperStrategy(ISqlMapperStrategy sqlMapperStrategy) {
        PumpConfig.sqlMapperStrategy = sqlMapperStrategy;
    }

    
    public static IRequestInfoFactory getRequestInfoFactory() {
        return getComponent(requestInfoFactory, IRequestInfoFactory.class);
    }

    
    public void setRequestInfoFactory(IRequestInfoFactory requestInfoFactory) {
        PumpConfig.requestInfoFactory = requestInfoFactory;
    }
    
    public static List<IAopInterceptor> getServiceAopInterceptors() {
        return serviceAopInterceptors;
    }

    
    public void setServiceAopInterceptors(List<IAopInterceptor> serviceAopInterceptors) {
        PumpConfig.serviceAopInterceptors = serviceAopInterceptors;
    }
}
