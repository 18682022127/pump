package com.itouch8.pump.core.dao.dialect;


public interface IDialect {

    
    enum DBType {
        Oracle, DB2, H2, MySql, ASE, IQ
    }

    
    public DBType getType();

    
    public String[] getDriverClassNames();

    
    public String getTotalSql(String sql);

    
    public String getScopeSql(String sql, long offset, int limit);
}
