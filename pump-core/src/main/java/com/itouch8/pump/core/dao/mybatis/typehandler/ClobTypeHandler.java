package com.itouch8.pump.core.dao.mybatis.typehandler;

import java.io.StringReader;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import com.itouch8.pump.core.util.annotation.Warning;


@Warning
public class ClobTypeHandler extends BaseTypeHandler<String> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException {
        try {
            StringReader reader = new StringReader(parameter);
            ps.setCharacterStream(i, reader, parameter.length());
        } catch (Exception e) {
            ps.setString(i, parameter);
        }
    }

    @Override
    public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
        try {
            String value = "";
            Clob clob = rs.getClob(columnName);
            if (clob != null) {
                int size = (int) clob.length();
                value = clob.getSubString(1, size);
            }
            return value;
        } catch (Exception e) {
            return rs.getString(columnName);
        }
    }

    @Override
    public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        try {
            String value = "";
            Clob clob = rs.getClob(columnIndex);
            if (clob != null) {
                int size = (int) clob.length();
                value = clob.getSubString(1, size);
            }
            return value;
        } catch (Exception e) {
            return rs.getString(columnIndex);
        }
    }

    @Override
    public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        try {
            String value = "";
            Clob clob = cs.getClob(columnIndex);
            if (clob != null) {
                int size = (int) clob.length();
                value = clob.getSubString(1, size);
            }
            return value;
        } catch (Exception e) {
            return cs.getString(columnIndex);
        }
    }
}
