package com.itouch8.pump.core.dao.sql.interceptor.impl;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.itouch8.pump.core.PumpConfig;
import com.itouch8.pump.core.dao.dialect.IDialect;
import com.itouch8.pump.core.dao.jndi.IJndi;
import com.itouch8.pump.core.dao.sql.resolver.ISqlResolver;


public class BaseSqlInterceptor extends AbstractSqlInterceptor {

    private final Properties properties;

    private Map<IDialect, Properties> dialectProperties;

    public BaseSqlInterceptor() {
        properties = new Properties();
        properties.put("tablePrefix", PumpConfig.getPumpTablePrefix());
        properties.put("BF", PumpConfig.getPumpTablePrefix() + "BF");
        super.setPrefix("{{");
        super.setSuffix("}}");
    }

    @Override
    protected String doIntercept(IJndi jndi, String expression, Object root) {
        try {
            Properties properties = getProperties();
            if (properties.containsKey(expression)) {
                return properties.getProperty(expression);
            } else {
                Properties dialectProperties = obtainDialectProperties(jndi);
                if (null != dialectProperties && dialectProperties.containsKey(expression)) {
                    return dialectProperties.getProperty(expression);
                } else {
                    ISqlResolver spr = getSqlResolver(jndi, expression);
                    if (null != spr) {
                        return spr.resolverSql(jndi, root, expression);
                    } else {
                        return expression;
                    }
                }
            }
        } catch (Exception e) {
            return expression;
        }
    }

    private ISqlResolver getSqlResolver(IJndi jndi, String propertyName) {
        List<? extends ISqlResolver> sprs = PumpConfig.getSqlResolvers();
        if (null != sprs && !sprs.isEmpty()) {
            for (ISqlResolver spr : sprs) {
                if (spr.isSupport(jndi, propertyName)) {
                    return spr;
                }
            }
        }
        return null;
    }

    private Properties obtainDialectProperties(IJndi jndi) {
        if (null != dialectProperties) {
            return dialectProperties.get(jndi.getDialect());
        }
        return null;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        if (null != properties) {
            this.properties.putAll(properties);
        }
    }

    public Map<IDialect, Properties> getDialectProperties() {
        return dialectProperties;
    }

    public void setDialectProperties(Map<IDialect, Properties> dialectProperties) {
        this.dialectProperties = dialectProperties;
    }
}
