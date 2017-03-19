package com.itouch8.pump.core.dao.util;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.itouch8.pump.core.util.exception.Throw;


public abstract class StatementUtilsImpl {

    private static final StatementUtilsImpl instance = new StatementUtilsImpl() {};

    private StatementUtilsImpl() {}

    static StatementUtilsImpl getInstance() {
        return instance;
    }

    
    public void setParameters(PreparedStatement ps, Object[] args) {
        setParameters(ps, args, null);
    }

    
    public void setParameters(PreparedStatement ps, Object[] args, int[] argTypes) {
        try {
            if (null != args) {
                if (null != argTypes && args.length == argTypes.length) {
                    for (int i = 0, l = args.length; i < l; i++) {
                        ps.setObject(i + 1, args[i], argTypes[i]);
                    }
                } else {
                    for (int i = 0, l = args.length; i < l; i++) {
                        ps.setObject(i + 1, args[i]);
                    }
                }
            }
        } catch (SQLException e) {
            throw Throw.createRuntimeException(e);
        }
    }
}
