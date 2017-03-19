package com.itouch8.pump.core.dao.mybatis.provider;

import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.mapping.DatabaseIdProvider;

import com.itouch8.pump.core.dao.dialect.IDialect;
import com.itouch8.pump.core.dao.jndi.JndiManager;


public class DialectDatabaseIdProvider implements DatabaseIdProvider {

    
    @Override
    public String getDatabaseId(DataSource dataSource) throws SQLException {
        IDialect dialect = JndiManager.getDialect(dataSource);
        return dialect.getType().name().toLowerCase();
    }

    @Override
    public void setProperties(Properties p) {}
}
