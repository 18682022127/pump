package com.itouch8.pump.core.dao.dialect.impl;

import java.util.regex.Pattern;

import com.itouch8.pump.core.dao.dialect.IDialect;


public abstract class SybaseASE extends AbstractDialect {

    private final static Pattern SELECT = Pattern.compile("select", Pattern.CASE_INSENSITIVE);

    // private final static Pattern FROM = Pattern.compile("from", Pattern.CASE_INSENSITIVE);

    // private final static Pattern ORDER = Pattern.compile("order by", Pattern.CASE_INSENSITIVE);

    private static final IDialect instance = new SybaseASE() {};// 唯一实例

    
    private SybaseASE() {
        super.setType(DBType.ASE);
        super.setDriverClassName("com.sybase.jdbc3.jdbc.SybDriver");
    }

    
    public static IDialect getSingleInstance() {
        return instance;
    }

    
    public String getTotalSql(String sql) {
        StringBuilder rs = new StringBuilder();
        int index = sql.toLowerCase().lastIndexOf("order by");
        if (-1 != index) {
            sql = sql.substring(0, index);
        }
        // sql = ORDER.matcher(sql).replaceAll("");//去掉子查询中的order by语句
        rs.append("select count(1) total_ from (").append(sql).append(") total_ ");
        return rs.toString();
    }

    
    public String getScopeSql(String sql, long offset, int limit) {
        sql = SELECT.matcher(sql).replaceFirst("select sybid=identity(12),");// 替换第一个匹配的select
        int index = sql.toLowerCase().indexOf("from");
        sql = sql.substring(0, index) + "into #temptable1 " + sql.substring(index);// 替换最后一个匹配的from
        // sql = FROM.matcher(sql).replaceFirst("into #temptable1 from");
        sql = " set chained off " + sql + " select * from #temptable1 where sybid> " + offset + " and sybid <= " + (offset + limit);
        sql += " drop table #temptable1 ";
        return sql;
    }
}
