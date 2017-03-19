package com.itouch8.pump.core.dao.dialect.impl;

import com.itouch8.pump.core.dao.dialect.IDialect;


public abstract class AbstractDialect implements IDialect {

    private DBType type;
    private String[] driverClassNames;

    
    @Override
    public DBType getType() {
        return type;
    }

    
    protected void setType(DBType type) {
        this.type = type;
    }

    
    @Override
    public String[] getDriverClassNames() {
        return driverClassNames;
    }

    
    protected void setDriverClassNames(String[] driverClassNames) {
        this.driverClassNames = driverClassNames;
    }

    
    protected void setDriverClassName(String driverClassName) {
        setDriverClassNames(new String[] {driverClassName});
    }
}
