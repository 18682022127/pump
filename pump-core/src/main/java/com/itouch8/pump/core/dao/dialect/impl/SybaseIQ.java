package com.itouch8.pump.core.dao.dialect.impl;

import java.util.regex.Pattern;

import com.itouch8.pump.core.dao.dialect.IDialect;


public abstract class SybaseIQ extends AbstractDialect {

    private static final IDialect instance = new SybaseIQ() {};// 唯一实例

    private final static Pattern SELECT = Pattern.compile("^\\s*select\\s+", Pattern.CASE_INSENSITIVE);

    private final static Pattern FROM = Pattern.compile("\\s+from\\s+", Pattern.CASE_INSENSITIVE);

    private final static Pattern ORDER = Pattern.compile("\\s+order\\s+by\\s+(?![^\\)]+\\))[^\\)]+$", Pattern.CASE_INSENSITIVE);

    
    private SybaseIQ() {
        super.setType(DBType.IQ);
        super.setDriverClassName("com.sybase.jdbc3.jdbc.SybDriver");
    }

    
    public static IDialect getSingleInstance() {
        return instance;
    }

    public String getTotalSql(String sql) {
        StringBuilder rs = new StringBuilder();
        sql = ORDER.matcher(sql).replaceFirst("");
        rs.append("select count(1) total_ from (").append(sql).append(") total_ ");
        return rs.toString();
    }

    public String getScopeSql(String sql, long offset, int limit) {
        String tempTableName = "#temptablename";
        sql = SELECT.matcher(sql).replaceFirst("SELECT TOP " + (offset + limit) + " ");
        sql = FROM.matcher(sql).replaceFirst(",PAGE_RECORD_ROW_NUMBER=NUMBER(*) INTO " + tempTableName + " FROM ");
        sql += " set chained off  SELECT * FROM " + tempTableName + " WHERE PAGE_RECORD_ROW_NUMBER > " + offset;
        sql += " drop table " + tempTableName;
        return sql;
    }
}
