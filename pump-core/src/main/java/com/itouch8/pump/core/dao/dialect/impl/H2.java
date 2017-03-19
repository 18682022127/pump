package com.itouch8.pump.core.dao.dialect.impl;

import com.itouch8.pump.core.dao.dialect.IDialect;


public abstract class H2 extends AbstractDialect {

    private static final IDialect instance = new H2() {};// 唯一实例

    
    private H2() {
        super.setType(DBType.H2);
        super.setDriverClassName("org.h2.Driver");
    }

    
    public static IDialect getSingleInstance() {
        return instance;
    }

    public String getTotalSql(String sql) {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT COUNT(1) FROM (").append(sql).append(") T");
        return sb.toString();
    }

    public String getScopeSql(String sql, long offset, int limit) {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT * FROM (").append(sql).append(") T LIMIT ").append(offset).append(",").append(limit);
        return sb.toString();
    }
}
