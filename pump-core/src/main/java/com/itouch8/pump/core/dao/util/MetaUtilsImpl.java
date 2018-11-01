package com.itouch8.pump.core.dao.util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.itouch8.pump.core.dao.util.DBHelp.IConnectionCallback;
import com.itouch8.pump.core.util.exception.Throw;


public abstract class MetaUtilsImpl {

    private static final MetaUtilsImpl instance = new MetaUtilsImpl() {};

    private MetaUtilsImpl() {}

    static MetaUtilsImpl getInstance() {
        return instance;
    }

    
    public DatabaseMetaData getDatabaseMetaData(DataSource dataSource) {
        return DBHelp.Connection.doInConnection(dataSource, new IConnectionCallback<DatabaseMetaData>() {
            @Override
            public DatabaseMetaData callback(Connection conn) {
                return getDatabaseMetaData(conn);
            }
        });
    }

    
    public DatabaseMetaData getDatabaseMetaData(Connection conn) {
        try {
            return conn.getMetaData();
        } catch (SQLException e) {
            throw Throw.createRuntimeException("pump.core.dao.database_meta", e);
        }
    }

    
    public String getDatabaseProductName(DataSource dataSource) {
        return DBHelp.Connection.doInConnection(dataSource, new IConnectionCallback<String>() {
            @Override
            public String callback(Connection conn) {
                return getDatabaseProductName(conn);
            }
        });
    }

    
    public String getDatabaseProductName(Connection conn) {
        try {
            return conn.getMetaData().getDatabaseProductName();
        } catch (SQLException e) {
            throw Throw.createRuntimeException("pump.core.dao.database_meta", e);
        }
    }

    
    public boolean exist(DataSource dataSource, final String tableName) {
        return DBHelp.Connection.doInConnection(dataSource, new IConnectionCallback<Boolean>() {
            @Override
            public Boolean callback(Connection conn) {
                return exist(conn, tableName);
            }
        });
    }

    
    public boolean exist(Connection conn, String tableName) {
        ResultSet rs = null;
        try {
            if (null == tableName) {
                return false;
            }
            DatabaseMetaData meta = conn.getMetaData();
            String[] t = getCatalogSchemaTable(tableName);
            rs = meta.getTables(t[0], t[1], t[2], new String[] {"TABLE"});
            return rs.next();
        } catch (SQLException e) {
            throw Throw.createRuntimeException("pump.core.dao.table_meta", e);
        } finally {
            DBHelp.Closer.close(rs);
        }
    }

    
    public boolean[] exist(DataSource dataSource, final String[] tableNames) {
        return DBHelp.Connection.doInConnection(dataSource, new IConnectionCallback<boolean[]>() {
            @Override
            public boolean[] callback(Connection conn) {
                return exist(conn, tableNames);
            }
        });
    }

    
    public boolean[] exist(Connection conn, String[] tableNames) {
        if (null == tableNames || 0 == tableNames.length) {
            return null;
        }
        boolean[] r = new boolean[tableNames.length];
        for (int i = 0, l = tableNames.length; i < l; i++) {
            String tableName = tableNames[i];
            r[i] = exist(conn, tableName);
        }
        return r;
    }

    private String[] getCatalogSchemaTable(String tableName) {
        String catalog = null;
        String schema = null;
        int index = tableName.lastIndexOf('.');
        if (-1 != index) {
            schema = tableName.substring(0, index);
            tableName = tableName.substring(index + 1);
            index = schema.lastIndexOf('.');
            if (-1 != index) {
                catalog = schema.substring(0, index);
                schema = schema.substring(index + 1);
            }
        }
        return new String[] {catalog, schema, tableName};
    }
}
