package com.itouch8.pump.core.dao.mybatis.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import com.itouch8.pump.core.util.CoreUtils;


public class String2NumberHandler extends BaseTypeHandler<String> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException {
        if (null == jdbcType) {
            ps.setString(i, parameter);
        } else {
            switch (jdbcType) {
                case SMALLINT:
                case INTEGER:
                case BIGINT:
                    if (CoreUtils.isBlank(parameter)) {
                        ps.setNull(i, jdbcType.TYPE_CODE);
                    } else {
                        ps.setInt(i, Integer.parseInt(parameter));
                    }
                    break;
                case FLOAT:
                case REAL:
                case DOUBLE:
                case NUMERIC:
                case DECIMAL:
                    if (CoreUtils.isBlank(parameter)) {
                        ps.setNull(i, jdbcType.TYPE_CODE);
                    } else {
                        ps.setDouble(i, Double.parseDouble(parameter));
                    }
                    break;
                default:
                    ps.setString(i, parameter);
                    break;
            }
        }
    }

    @Override
    public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return rs.getString(columnName);
    }

    @Override
    public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getString(columnIndex);
    }

    @Override
    public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return cs.getString(columnIndex);
    }
}
