package com.itouch8.pump.core.dao.sql;

import java.util.List;
import java.util.Map;

import com.itouch8.pump.core.PumpConfig;
import com.itouch8.pump.core.dao.jndi.IJndi;
import com.itouch8.pump.core.dao.sql.interceptor.ISqlInterceptor;


public class SqlManager {

    
    public static String getExecuteSqlId(String sqlId) {
        Map<String, String> sqlIdMapping = PumpConfig.getSqlIdMapping();
        if (null != sqlIdMapping && null != sqlId && null != sqlIdMapping.get(sqlId)) {
            return sqlIdMapping.get(sqlId);
        } else {
            return sqlId;
        }
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
