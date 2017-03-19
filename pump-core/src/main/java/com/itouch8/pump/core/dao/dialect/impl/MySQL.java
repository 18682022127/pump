package com.itouch8.pump.core.dao.dialect.impl;

import com.itouch8.pump.core.dao.dialect.IDialect;


public abstract class MySQL extends AbstractDialect {

    private static final IDialect instance = new MySQL() {};// 唯一实例

    
    private MySQL() {
        super.setType(DBType.MySql);
        super.setDriverClassName("com.mysql.jdbc.Driver");
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
