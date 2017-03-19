package com.itouch8.pump.core.dao.util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.itouch8.pump.core.util.CoreUtils;
import com.itouch8.pump.core.util.exception.Throw;


public abstract class ResultSetUtilsImpl {

    private static final ResultSetUtilsImpl instance = new ResultSetUtilsImpl() {};

    private ResultSetUtilsImpl() {}

    static ResultSetUtilsImpl getInstance() {
        return instance;
    }

    
    public List<Map<String, Object>> handlerResultSet(ResultSet rs) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            if (null != rs) {
                String[][] fields = getFields(rs);
                int length = fields[0].length;
                while (rs.next()) {
                    Map<String, Object> map = new LinkedHashMap<String, Object>();
                    for (int i = 0; i < length; i++) {
                        Object value = rs.getObject(fields[0][i]);
                        map.put(fields[1][i], rs.wasNull() ? null : value);
                    }
                    list.add(map);
                }
            }
        } catch (SQLException e) {
            Throw.throwRuntimeException(e);
        }
        return list;
    }

    
    public String[][] getFields(ResultSet rs) {
        String[][] fields = null;
        try {
            ResultSetMetaData meta = rs.getMetaData();
            int length = meta.getColumnCount();
            fields = new String[2][length];
            for (int i = 1; i <= length; i++) {
                fields[0][i - 1] = meta.getColumnLabel(i);
                fields[1][i - 1] = CoreUtils.convertToCamel(fields[0][i - 1]);
            }
        } catch (SQLException e) {
            Throw.throwRuntimeException(e);
        }
        return fields;
    }

}
