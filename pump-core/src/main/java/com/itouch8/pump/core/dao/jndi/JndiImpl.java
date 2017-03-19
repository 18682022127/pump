package com.itouch8.pump.core.dao.jndi;

import java.sql.Connection;
import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.InitializingBean;

import com.itouch8.pump.core.dao.dialect.IDialect;
import com.itouch8.pump.core.dao.util.DBHelp;
import com.itouch8.pump.core.dao.util.DBHelp.IConnectionCallback;


public class JndiImpl implements IJndi, InitializingBean {

    private String name; // 名称
    private boolean isDefault; // 是否默认
    private DataSource dataSource; // 数据源
    private IDialect dialect; // 数据库方言
    private Properties properties;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isDefault() {
        return isDefault;
    }

    @Override
    public IDialect getDialect() {
        return dialect;
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDialect(IDialect dialect) {
        if (null != dialect) {
            this.dialect = dialect;
        }
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        if (null == this.dialect) {
            this.dialect = DBHelp.Connection.doInConnection(dataSource, new IConnectionCallback<IDialect>() {
                @Override
                public IDialect callback(Connection conn) {
                    return JndiManager.getDialect(conn);
                }
            });
        }
    }

    @Override
    public Properties getProperties() {
        return this.properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        JndiManager.register(this);
    }
}
