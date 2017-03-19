package com.itouch8.pump.core.dao.jndi;

import java.util.Properties;

import javax.sql.DataSource;

import com.itouch8.pump.core.dao.dialect.IDialect;


public interface IJndi {

    
    public String getName();

    
    public boolean isDefault();

    
    public IDialect getDialect();

    
    public DataSource getDataSource();

    
    public Properties getProperties();
}
