package com.itouch8.pump.core.dao.sql;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import com.itouch8.pump.core.PumpConfig;
import com.itouch8.pump.core.dao.annotation.SqlRef;
import com.itouch8.pump.core.dao.jndi.IJndi;
import com.itouch8.pump.core.dao.sql.interceptor.ISqlInterceptor;
import com.itouch8.pump.core.dao.sql.mapper.ISqlMapperStrategy;
import com.itouch8.pump.core.util.CoreUtils;


public class SqlManager {

    
    public static String getExecuteSqlId(String sqlId) {
        Map<String, String> sqlIdMapping = PumpConfig.getSqlIdMapping();
        if (null != sqlIdMapping && null != sqlId && null != sqlIdMapping.get(sqlId)) {
            return sqlIdMapping.get(sqlId);
        } else {
            return sqlId;
        }
    }

    
    public static String getSqlId(Method method) {
        ISqlMapperStrategy sqlMapperStrategy = PumpConfig.getSqlMapperStrategy();
        if (null != sqlMapperStrategy) {
            return sqlMapperStrategy.lookup(method);
        }
        return null;
    }

    
    public static String resolverSqlId(SqlRef sqlRef, Method method) {
        String rs = sqlRef.value();
        if (CoreUtils.isBlank(rs)) {
            rs = method.getName();
        }

        if (sqlRef.classpath()) {
            rs = method.getDeclaringClass().getName() + "." + rs;
        }
        return rs;
    }

    
    public static String doIntercept(IJndi jndi, String src, Object root) {
        List<? extends ISqlInterceptor> is = PumpConfig.getSqlInterceptors();
        if (null != is && !is.isEmpty()) {
            for (ISqlInterceptor s : is) {
                src = s.intercept(jndi, src, root);
            }
        }
        return src;
    }
}
