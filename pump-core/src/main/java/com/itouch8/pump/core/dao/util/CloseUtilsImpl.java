package com.itouch8.pump.core.dao.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.itouch8.pump.core.util.exception.Throw;


public abstract class CloseUtilsImpl {

    private static final CloseUtilsImpl instance = new CloseUtilsImpl() {};

    private CloseUtilsImpl() {}

    static CloseUtilsImpl getInstance() {
        return instance;
    }

    
    public void close(Connection conn) {
        try {
            if (null != conn && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            Throw.throwRuntimeException(e);
        }
    }

    
    public void close(ResultSet rs) {
        try {
            if (null != rs) {
                rs.close();
            }
        } catch (SQLException e) {
            Throw.throwRuntimeException(e);
        }
    }

    
    public void close(Statement stat) {
        try {
            if (null != stat) {
                stat.close();
            }
        } catch (SQLException e) {
            Throw.throwRuntimeException(e);
        }
    }

    
    public void close(Statement stat, ResultSet rs) {
        close(rs);
        close(stat);
    }

    
    public void close(Connection conn, Statement stat, ResultSet rs) {
        close(rs);
        close(stat);
        close(conn);
    }

    
    public void close(Connection conn, Statement stat) {
        close(stat);
        close(conn);
    }
}
