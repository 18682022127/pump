package com.itouch8.pump.core.dao.jndi;

import java.sql.Connection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.itouch8.pump.ReturnCodes;
import com.itouch8.pump.core.PumpConfig;
import com.itouch8.pump.core.dao.dialect.IDialect;
import com.itouch8.pump.core.dao.util.DBHelp;
import com.itouch8.pump.core.dao.util.DBHelp.IConnectionCallback;
import com.itouch8.pump.core.util.CoreUtils;
import com.itouch8.pump.core.util.exception.Throw;

public class JndiManager implements ApplicationContextAware, InitializingBean {

    private static IJndi DEFAULT = null;
    // private static AtomicBoolean monitor = new AtomicBoolean(false);
    private static final Map<String, IJndi> jndiCache = new LinkedHashMap<String, IJndi>();
    private static final Map<DataSource, PlatformTransactionManager> dataSources = new HashMap<DataSource, PlatformTransactionManager>();
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        JndiManager.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, DataSource> dataSources = applicationContext.getBeansOfType(DataSource.class);
        if (null != dataSources && !dataSources.isEmpty()) {
            for (Iterator<String> i = dataSources.keySet().iterator(); i.hasNext();) {
                String beanId = i.next();
                register(beanId, dataSources.get(beanId));
            }
        }
        initialDefaultJndi();
    }

    private static void register(String beanName, DataSource dataSource) {
        if (dataSources.containsKey(dataSource)) {
            return;
        } else {
            JndiImpl jndi = new JndiImpl();
            jndi.setName(beanName);
            jndi.setDataSource(dataSource);
            jndiCache.put(beanName, jndi);
            dataSources.put(dataSource, resolverTransactionManager(dataSource));
        }
    }

    static void register(JndiImpl jndi) {
        DataSource dataSource = jndi.getDataSource();
        if (!dataSources.containsKey(dataSource)) {
            jndiCache.put(jndi.getName(), jndi);
            dataSources.put(dataSource, resolverTransactionManager(dataSource));
        }
    }

    
    public static Map<String, IJndi> getJndis() {
        return Collections.unmodifiableMap(jndiCache);
    }

    
    public static IJndi getDefaultJndi() {
        // if(!monitor.get()){
        // synchronized(monitor){
        // if(!monitor.get()){
        // //initialDefaultJndi();
        // if(DEFAULT != null){
        // monitor.set(true);
        // }
        // }
        // }
        // }
        return DEFAULT;
    }

    
    public static PlatformTransactionManager getTransactinManager(DataSource dataSource) {
        return dataSources.get(dataSource);
    }

    
    public static IJndi getJndi(String name) {
        if (CoreUtils.isBlank(name)) {
            return getDefaultJndi();
        } else {
            IJndi jndi = jndiCache.get(name);
            if (null == jndi) {
                throw Throw.createRuntimeException(ReturnCodes.SYSTEM_ERROR.code, "pump.core.dao.not_found_datasource", name);
            }
            return jndi;
        }
    }

    
    public static IJndi getJndi(DataSource dataSource) {
        for (IJndi j : jndiCache.values()) {
            if (dataSource.equals(j.getDataSource())) {
                return j;
            }
        }
        return getDefaultJndi();
    }

    
    public static IDialect getDialect(IJndi jndi) {
        return jndi.getDialect();
    }

    
    public static IDialect getDialect(DataSource dataSource) {
        IJndi jndi = getJndi(dataSource);
        if (null == jndi) {
            return DBHelp.Connection.doInConnection(dataSource, new IConnectionCallback<IDialect>() {
                @Override
                public IDialect callback(Connection conn) {
                    return getDialect(conn);
                }
            });
        } else {
            return jndi.getDialect();
        }
    }

    
    public static IDialect getDialect(Connection conn) {
        String databaseProductName = DBHelp.Meta.getDatabaseProductName(conn);
        return getDialect(databaseProductName);
    }

    
    private static IDialect getDialect(String databaseProductName) {
        Map<String, IDialect> databaseProductNameDialectMapping = PumpConfig.getDatabaseProductNameDialectMapping();
        if (CoreUtils.isBlank(databaseProductName)) {
            Throw.throwRuntimeException(ReturnCodes.SYSTEM_ERROR.code,"pump.core.dao.database_product_name_is_empty");
        } else if (null == databaseProductNameDialectMapping) {
            Throw.throwRuntimeException(ReturnCodes.SYSTEM_ERROR.code,"pump.core.dao.not_config_dialect_mapping");
        } else {
            for (String key : databaseProductNameDialectMapping.keySet()) {
                if (null != key && -1 != databaseProductName.toLowerCase().indexOf(key.toLowerCase())) {
                    return databaseProductNameDialectMapping.get(key);
                }
            }
            Throw.throwRuntimeException(ReturnCodes.SYSTEM_ERROR.code,"pump.core.dao.not_found_dialect", databaseProductName);
        }
        return null;
    }

    private static void initialDefaultJndi() {
        IJndi first = null;
        IJndi defaultName = null;
        for (String name : jndiCache.keySet()) {
            IJndi jndi = jndiCache.get(name);
            if (jndi.isDefault()) {
                DEFAULT = jndi;
                return;
            }
            if (null == first) {
                first = jndi;
            }
            if (null == defaultName && "dataSource".equals(name)) {
                defaultName = jndi;
            }
        }
        if (null == DEFAULT) {
            if (null != defaultName) {
                DEFAULT = defaultName;
                return;
            }
            if (null != first) {
                DEFAULT = first;
                return;
            }
        }
        if (DEFAULT instanceof JndiImpl) {
            ((JndiImpl) DEFAULT).setDefault(true);
        }
    }

    private static PlatformTransactionManager resolverTransactionManager(DataSource ds) {
        Map<String, PlatformTransactionManager> tms = applicationContext.getBeansOfType(PlatformTransactionManager.class);
        if (null != tms) {
            for (PlatformTransactionManager tm : tms.values()) {
                if (tm instanceof DataSourceTransactionManager) {
                    DataSourceTransactionManager dstm = (DataSourceTransactionManager) tm;
                    if (ds.equals(dstm.getDataSource())) {
                        return dstm;
                    }
                }
            }
        }
        return null;
    }
}
