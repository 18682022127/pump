package com.itouch8.pump.core.dao.dialect.impl;

import com.itouch8.pump.core.dao.dialect.IDialect;


public abstract class Db2 extends AbstractDialect {

    private static final IDialect instance = new Db2() {};// 唯一实例

    
    private Db2() {
        super.setType(DBType.DB2);
        super.setDriverClassName("com.ibm.db2.jcc.DB2Driver");
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
        scope.append("select * from ( select row_.*, row_number() over() rownum_ from ( ");
        scope.append(sql);
        scope.append(" ) row_ ) a where rownum_ > ").append(offset).append(" and rownum_ <= ").append(offset + limit);
        return scope.toString();
    }
}
