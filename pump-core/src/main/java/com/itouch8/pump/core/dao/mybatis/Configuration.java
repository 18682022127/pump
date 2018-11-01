package com.itouch8.pump.core.dao.mybatis;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.ibatis.binding.MapperRegistry;
import org.apache.ibatis.builder.CacheRefResolver;
import org.apache.ibatis.builder.ResultMapResolver;
import org.apache.ibatis.builder.annotation.MethodResolver;
import org.apache.ibatis.builder.xml.XMLStatementBuilder;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.executor.CachingExecutor;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.loader.ProxyFactory;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMap;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.scripting.LanguageDriverRegistry;
import org.apache.ibatis.session.AutoMappingBehavior;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.LocalCacheScope;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.transaction.Transaction;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeAliasRegistry;
import org.apache.ibatis.type.TypeHandlerRegistry;

import com.itouch8.pump.core.dao.jndi.IJndi;
import com.itouch8.pump.core.dao.mybatis.executor.BatchExecutor;
import com.itouch8.pump.core.dao.mybatis.executor.ReuseExecutor;
import com.itouch8.pump.core.dao.mybatis.executor.SimpleExecutor;

public class Configuration extends org.apache.ibatis.session.Configuration {

    private org.apache.ibatis.session.Configuration delegate;

    private IJndi jndi;

    public Configuration(org.apache.ibatis.session.Configuration delegate, IJndi jndi) {
        this.delegate = delegate;
        this.jndi = jndi;
    }

    public org.apache.ibatis.session.Configuration getDelegate() {
        return delegate;
    }

    @Override
    public String getLogPrefix() {
        // TODO Auto-generated method stub
        return delegate.getLogPrefix();
    }

    @Override
    public void setLogPrefix(String logPrefix) {
        // TODO Auto-generated method stub
        delegate.setLogPrefix(logPrefix);
    }

    @Override
    public Class<? extends Log> getLogImpl() {
        return delegate.getLogImpl();
    }

    @SuppressWarnings("unchecked")
	@Override
    public void setLogImpl(@SuppressWarnings("rawtypes") Class logImpl) {
        delegate.setLogImpl(logImpl);
    }

    @Override
    public boolean isCallSettersOnNulls() {
        return delegate.isCallSettersOnNulls();
    }

    @Override
    public void setCallSettersOnNulls(boolean callSettersOnNulls) {
        delegate.setCallSettersOnNulls(callSettersOnNulls);
    }

    @Override
    public String getDatabaseId() {
        return delegate.getDatabaseId();
    }

    @Override
    public void setDatabaseId(String databaseId) {
        delegate.setDatabaseId(databaseId);
    }

    @Override
    public Class<?> getConfigurationFactory() {
        return delegate.getConfigurationFactory();
    }

    @Override
    public void setConfigurationFactory(Class<?> configurationFactory) {
        // TODO Auto-generated method stub
        delegate.setConfigurationFactory(configurationFactory);
    }

    @Override
    public boolean isSafeResultHandlerEnabled() {
        return delegate.isSafeResultHandlerEnabled();
    }

    @Override
    public void setSafeResultHandlerEnabled(boolean safeResultHandlerEnabled) {
        delegate.setSafeResultHandlerEnabled(safeResultHandlerEnabled);
    }

    @Override
    public boolean isSafeRowBoundsEnabled() {
        return delegate.isSafeRowBoundsEnabled();
    }

    @Override
    public void setSafeRowBoundsEnabled(boolean safeRowBoundsEnabled) {
        delegate.setSafeRowBoundsEnabled(safeRowBoundsEnabled);
    }

    @Override
    public boolean isMapUnderscoreToCamelCase() {
        return delegate.isMapUnderscoreToCamelCase();
    }

    @Override
    public void setMapUnderscoreToCamelCase(boolean mapUnderscoreToCamelCase) {
        // TODO Auto-generated method stub
        delegate.setMapUnderscoreToCamelCase(mapUnderscoreToCamelCase);
    }

    @Override
    public void addLoadedResource(String resource) {
        // TODO Auto-generated method stub
        delegate.addLoadedResource(resource);
    }

    @Override
    public boolean isResourceLoaded(String resource) {
        // TODO Auto-generated method stub
        return delegate.isResourceLoaded(resource);
    }

    @Override
    public Environment getEnvironment() {
        // TODO Auto-generated method stub
        return delegate.getEnvironment();
    }

    @Override
    public void setEnvironment(Environment environment) {
        // TODO Auto-generated method stub
        delegate.setEnvironment(environment);
    }

    @Override
    public AutoMappingBehavior getAutoMappingBehavior() {
        // TODO Auto-generated method stub
        return delegate.getAutoMappingBehavior();
    }

    @Override
    public void setAutoMappingBehavior(AutoMappingBehavior autoMappingBehavior) {
        // TODO Auto-generated method stub
        delegate.setAutoMappingBehavior(autoMappingBehavior);
    }

    @Override
    public boolean isLazyLoadingEnabled() {
        // TODO Auto-generated method stub
        return delegate.isLazyLoadingEnabled();
    }

    @Override
    public void setLazyLoadingEnabled(boolean lazyLoadingEnabled) {
        // TODO Auto-generated method stub
        delegate.setLazyLoadingEnabled(lazyLoadingEnabled);
    }

    @Override
    public ProxyFactory getProxyFactory() {
        // TODO Auto-generated method stub
        return delegate.getProxyFactory();
    }

    @Override
    public void setProxyFactory(ProxyFactory proxyFactory) {
        // TODO Auto-generated method stub
        delegate.setProxyFactory(proxyFactory);
    }

    @Override
    public boolean isAggressiveLazyLoading() {
        // TODO Auto-generated method stub
        return delegate.isAggressiveLazyLoading();
    }

    @Override
    public void setAggressiveLazyLoading(boolean aggressiveLazyLoading) {
        // TODO Auto-generated method stub
        delegate.setAggressiveLazyLoading(aggressiveLazyLoading);
    }

    @Override
    public boolean isMultipleResultSetsEnabled() {
        // TODO Auto-generated method stub
        return delegate.isMultipleResultSetsEnabled();
    }

    @Override
    public void setMultipleResultSetsEnabled(boolean multipleResultSetsEnabled) {
        // TODO Auto-generated method stub
        delegate.setMultipleResultSetsEnabled(multipleResultSetsEnabled);
    }

    @Override
    public Set<String> getLazyLoadTriggerMethods() {
        // TODO Auto-generated method stub
        return delegate.getLazyLoadTriggerMethods();
    }

    @Override
    public void setLazyLoadTriggerMethods(Set<String> lazyLoadTriggerMethods) {
        // TODO Auto-generated method stub
        delegate.setLazyLoadTriggerMethods(lazyLoadTriggerMethods);
    }

    @Override
    public boolean isUseGeneratedKeys() {
        // TODO Auto-generated method stub
        return delegate.isUseGeneratedKeys();
    }

    @Override
    public void setUseGeneratedKeys(boolean useGeneratedKeys) {
        // TODO Auto-generated method stub
        delegate.setUseGeneratedKeys(useGeneratedKeys);
    }

    @Override
    public ExecutorType getDefaultExecutorType() {
        // TODO Auto-generated method stub
        return delegate.getDefaultExecutorType();
    }

    @Override
    public void setDefaultExecutorType(ExecutorType defaultExecutorType) {
        // TODO Auto-generated method stub
        delegate.setDefaultExecutorType(defaultExecutorType);
    }

    @Override
    public boolean isCacheEnabled() {
        // TODO Auto-generated method stub
        return delegate.isCacheEnabled();
    }

    @Override
    public void setCacheEnabled(boolean cacheEnabled) {
        // TODO Auto-generated method stub
        delegate.setCacheEnabled(cacheEnabled);
    }

    @Override
    public Integer getDefaultStatementTimeout() {
        // TODO Auto-generated method stub
        return delegate.getDefaultStatementTimeout();
    }

    @Override
    public void setDefaultStatementTimeout(Integer defaultStatementTimeout) {
        // TODO Auto-generated method stub
        delegate.setDefaultStatementTimeout(defaultStatementTimeout);
    }

    @Override
    public Integer getDefaultFetchSize() {
        // TODO Auto-generated method stub
        return delegate.getDefaultFetchSize();
    }

    @Override
    public void setDefaultFetchSize(Integer defaultFetchSize) {
        // TODO Auto-generated method stub
        delegate.setDefaultFetchSize(defaultFetchSize);
    }

    @Override
    public boolean isUseColumnLabel() {
        // TODO Auto-generated method stub
        return delegate.isUseColumnLabel();
    }

    @Override
    public void setUseColumnLabel(boolean useColumnLabel) {
        // TODO Auto-generated method stub
        delegate.setUseColumnLabel(useColumnLabel);
    }

    @Override
    public LocalCacheScope getLocalCacheScope() {
        // TODO Auto-generated method stub
        return delegate.getLocalCacheScope();
    }

    @Override
    public void setLocalCacheScope(LocalCacheScope localCacheScope) {
        // TODO Auto-generated method stub
        delegate.setLocalCacheScope(localCacheScope);
    }

    @Override
    public JdbcType getJdbcTypeForNull() {
        // TODO Auto-generated method stub
        return delegate.getJdbcTypeForNull();
    }

    @Override
    public void setJdbcTypeForNull(JdbcType jdbcTypeForNull) {
        // TODO Auto-generated method stub
        delegate.setJdbcTypeForNull(jdbcTypeForNull);
    }

    @Override
    public Properties getVariables() {
        // TODO Auto-generated method stub
        return delegate.getVariables();
    }

    @Override
    public void setVariables(Properties variables) {
        // TODO Auto-generated method stub
        delegate.setVariables(variables);
    }

    @Override
    public TypeHandlerRegistry getTypeHandlerRegistry() {
        // TODO Auto-generated method stub
        return delegate.getTypeHandlerRegistry();
    }

    @Override
    public TypeAliasRegistry getTypeAliasRegistry() {
        // TODO Auto-generated method stub
        return delegate.getTypeAliasRegistry();
    }

    @Override
    public MapperRegistry getMapperRegistry() {
        // TODO Auto-generated method stub
        return delegate.getMapperRegistry();
    }

    @Override
    public ReflectorFactory getReflectorFactory() {
        // TODO Auto-generated method stub
        return delegate.getReflectorFactory();
    }

    @Override
    public void setReflectorFactory(ReflectorFactory reflectorFactory) {
        // TODO Auto-generated method stub
        delegate.setReflectorFactory(reflectorFactory);
    }

    @Override
    public ObjectFactory getObjectFactory() {
        // TODO Auto-generated method stub
        return delegate.getObjectFactory();
    }

    @Override
    public void setObjectFactory(ObjectFactory objectFactory) {
        // TODO Auto-generated method stub
        delegate.setObjectFactory(objectFactory);
    }

    @Override
    public ObjectWrapperFactory getObjectWrapperFactory() {
        // TODO Auto-generated method stub
        return delegate.getObjectWrapperFactory();
    }

    @Override
    public void setObjectWrapperFactory(ObjectWrapperFactory objectWrapperFactory) {
        // TODO Auto-generated method stub
        delegate.setObjectWrapperFactory(objectWrapperFactory);
    }

    @Override
    public List<Interceptor> getInterceptors() {
        // TODO Auto-generated method stub
        return delegate.getInterceptors();
    }

    @Override
    public LanguageDriverRegistry getLanguageRegistry() {
        // TODO Auto-generated method stub
        return delegate.getLanguageRegistry();
    }

    @Override
    public void setDefaultScriptingLanguage(Class<?> driver) {
        // TODO Auto-generated method stub
        delegate.setDefaultScriptingLanguage(driver);
    }

    @Override
    public LanguageDriver getDefaultScriptingLanuageInstance() {
        // TODO Auto-generated method stub
        return delegate.getDefaultScriptingLanuageInstance();
    }

    @Override
    public MetaObject newMetaObject(Object object) {
        // TODO Auto-generated method stub
        return delegate.newMetaObject(object);
    }

    @Override
    public ParameterHandler newParameterHandler(MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql) {
        return delegate.newParameterHandler(mappedStatement, parameterObject, boundSql);
    }

    @Override
    public ResultSetHandler newResultSetHandler(Executor executor, MappedStatement mappedStatement, RowBounds rowBounds, ParameterHandler parameterHandler, @SuppressWarnings("rawtypes") ResultHandler resultHandler, BoundSql boundSql) {
        // TODO Auto-generated method stub
        return delegate.newResultSetHandler(executor, mappedStatement, rowBounds, parameterHandler, resultHandler, boundSql);
    }

    @Override
    public StatementHandler newStatementHandler(Executor executor, MappedStatement mappedStatement, Object parameterObject, RowBounds rowBounds, @SuppressWarnings("rawtypes") ResultHandler resultHandler, BoundSql boundSql) {
        // TODO Auto-generated method stub
        return delegate.newStatementHandler(executor, mappedStatement, parameterObject, rowBounds, resultHandler, boundSql);
    }

    @Override
    public Executor newExecutor(Transaction transaction) {
        // TODO Auto-generated method stub
        return delegate.newExecutor(transaction);
    }

    @Override
    public Executor newExecutor(Transaction transaction, ExecutorType executorType) {
        executorType = executorType == null ? defaultExecutorType : executorType;
        executorType = executorType == null ? ExecutorType.SIMPLE : executorType;
        Executor executor;
        if (ExecutorType.BATCH == executorType) {
            executor = new BatchExecutor(this, transaction, jndi);
        } else if (ExecutorType.REUSE == executorType) {
            executor = new ReuseExecutor(this, transaction, jndi);
        } else {
            executor = new SimpleExecutor(this, transaction, jndi);
        }
        if (cacheEnabled) {
            executor = new CachingExecutor(executor);
        }
        executor = (Executor) interceptorChain.pluginAll(executor);
        return executor;
    }

    @Override
    public void addKeyGenerator(String id, KeyGenerator keyGenerator) {
        // TODO Auto-generated method stub
        delegate.addKeyGenerator(id, keyGenerator);
    }

    @Override
    public Collection<String> getKeyGeneratorNames() {
        // TODO Auto-generated method stub
        return delegate.getKeyGeneratorNames();
    }

    @Override
    public Collection<KeyGenerator> getKeyGenerators() {
        // TODO Auto-generated method stub
        return delegate.getKeyGenerators();
    }

    @Override
    public KeyGenerator getKeyGenerator(String id) {
        // TODO Auto-generated method stub
        return delegate.getKeyGenerator(id);
    }

    @Override
    public boolean hasKeyGenerator(String id) {
        // TODO Auto-generated method stub
        return delegate.hasKeyGenerator(id);
    }

    @Override
    public void addCache(Cache cache) {
        // TODO Auto-generated method stub
        delegate.addCache(cache);
    }

    @Override
    public Collection<String> getCacheNames() {
        // TODO Auto-generated method stub
        return delegate.getCacheNames();
    }

    @Override
    public Collection<Cache> getCaches() {
        // TODO Auto-generated method stub
        return delegate.getCaches();
    }

    @Override
    public Cache getCache(String id) {
        // TODO Auto-generated method stub
        return delegate.getCache(id);
    }

    @Override
    public boolean hasCache(String id) {
        // TODO Auto-generated method stub
        return delegate.hasCache(id);
    }

    @Override
    public void addResultMap(ResultMap rm) {
        // TODO Auto-generated method stub
        delegate.addResultMap(rm);
    }

    @Override
    public Collection<String> getResultMapNames() {
        // TODO Auto-generated method stub
        return delegate.getResultMapNames();
    }

    @Override
    public Collection<ResultMap> getResultMaps() {
        // TODO Auto-generated method stub
        return delegate.getResultMaps();
    }

    @Override
    public ResultMap getResultMap(String id) {
        // TODO Auto-generated method stub
        return delegate.getResultMap(id);
    }

    @Override
    public boolean hasResultMap(String id) {
        // TODO Auto-generated method stub
        return delegate.hasResultMap(id);
    }

    @Override
    public void addParameterMap(ParameterMap pm) {
        // TODO Auto-generated method stub
        delegate.addParameterMap(pm);
    }

    @Override
    public Collection<String> getParameterMapNames() {
        // TODO Auto-generated method stub
        return delegate.getParameterMapNames();
    }

    @Override
    public Collection<ParameterMap> getParameterMaps() {
        // TODO Auto-generated method stub
        return delegate.getParameterMaps();
    }

    @Override
    public ParameterMap getParameterMap(String id) {
        // TODO Auto-generated method stub
        return delegate.getParameterMap(id);
    }

    @Override
    public boolean hasParameterMap(String id) {
        // TODO Auto-generated method stub
        return delegate.hasParameterMap(id);
    }

    @Override
    public void addMappedStatement(MappedStatement ms) {
        // TODO Auto-generated method stub
        delegate.addMappedStatement(ms);
    }

    @Override
    public Collection<String> getMappedStatementNames() {
        // TODO Auto-generated method stub
        return delegate.getMappedStatementNames();
    }

    @Override
    public Collection<MappedStatement> getMappedStatements() {
        // TODO Auto-generated method stub
        return delegate.getMappedStatements();
    }

    @Override
    public Collection<XMLStatementBuilder> getIncompleteStatements() {
        // TODO Auto-generated method stub
        return delegate.getIncompleteStatements();
    }

    @Override
    public void addIncompleteStatement(XMLStatementBuilder incompleteStatement) {
        // TODO Auto-generated method stub
        delegate.addIncompleteStatement(incompleteStatement);
    }

    @Override
    public Collection<CacheRefResolver> getIncompleteCacheRefs() {
        // TODO Auto-generated method stub
        return delegate.getIncompleteCacheRefs();
    }

    @Override
    public void addIncompleteCacheRef(CacheRefResolver incompleteCacheRef) {
        // TODO Auto-generated method stub
        delegate.addIncompleteCacheRef(incompleteCacheRef);
    }

    @Override
    public Collection<ResultMapResolver> getIncompleteResultMaps() {
        // TODO Auto-generated method stub
        return delegate.getIncompleteResultMaps();
    }

    @Override
    public void addIncompleteResultMap(ResultMapResolver resultMapResolver) {
        // TODO Auto-generated method stub
        delegate.addIncompleteResultMap(resultMapResolver);
    }

    @Override
    public void addIncompleteMethod(MethodResolver builder) {
        // TODO Auto-generated method stub
        delegate.addIncompleteMethod(builder);
    }

    @Override
    public Collection<MethodResolver> getIncompleteMethods() {
        // TODO Auto-generated method stub
        return delegate.getIncompleteMethods();
    }

    @Override
    public MappedStatement getMappedStatement(String id) {
        // TODO Auto-generated method stub
        return delegate.getMappedStatement(id);
    }

    @Override
    public MappedStatement getMappedStatement(String id, boolean validateIncompleteStatements) {
        // TODO Auto-generated method stub
        return delegate.getMappedStatement(id, validateIncompleteStatements);
    }

    @Override
    public Map<String, XNode> getSqlFragments() {
        // TODO Auto-generated method stub
        return delegate.getSqlFragments();
    }

    @Override
    public void addInterceptor(Interceptor interceptor) {
        // TODO Auto-generated method stub
        delegate.addInterceptor(interceptor);
    }

    @Override
    public void addMappers(String packageName, Class<?> delegateType) {
        // TODO Auto-generated method stub
        delegate.addMappers(packageName, delegateType);
    }

    @Override
    public void addMappers(String packageName) {
        // TODO Auto-generated method stub
        delegate.addMappers(packageName);
    }

    @Override
    public <T> void addMapper(Class<T> type) {
        // TODO Auto-generated method stub
        delegate.addMapper(type);
    }

    @Override
    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        // TODO Auto-generated method stub
        return delegate.getMapper(type, sqlSession);
    }

    @Override
    public boolean hasMapper(Class<?> type) {
        // TODO Auto-generated method stub
        return delegate.hasMapper(type);
    }

    @Override
    public boolean hasStatement(String statementName) {
        // TODO Auto-generated method stub
        return delegate.hasStatement(statementName);
    }

    @Override
    public boolean hasStatement(String statementName, boolean validateIncompleteStatements) {
        // TODO Auto-generated method stub
        return delegate.hasStatement(statementName, validateIncompleteStatements);
    }

    @Override
    public void addCacheRef(String namespace, String referencedNamespace) {
        // TODO Auto-generated method stub
        delegate.addCacheRef(namespace, referencedNamespace);
    }
}
