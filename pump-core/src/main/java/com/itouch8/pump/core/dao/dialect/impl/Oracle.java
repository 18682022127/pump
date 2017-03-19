package com.itouch8.pump.core.dao.dialect.impl;

import com.itouch8.pump.core.dao.dialect.IDialect;


public abstract class Oracle extends AbstractDialect {

    private static final IDialect instance = new Oracle() {};// 唯一实例

    
    private Oracle() {
        super.setType(DBType.Oracle);
        super.setDriverClassName("oracle.jdbc.driver.OracleDriver");
    }

    
    public static IDialect getSingleInstance() {
        return instance;
    }

    
    public String getTotalSql(String sql) {
        StringBuilder rs = new StringBuilder();
        rs.append("select count(1) total_ from (").append(sql).append(") total_ ");
        return rs.toString();
    }

    
    public String getScopeSql(String sql, long offset, int limit) {
        StringBuilder scope = new StringBuilder();
        scope.append("select * from ( select row_.*, rownum rownum_ from ( ");
        scope.append(sql);
        scope.append(" ) row_ ) where rownum_ > ").append(offset).append(" and rownum_ <= ").append(offset + limit);
        return scope.toString();
    }
}
