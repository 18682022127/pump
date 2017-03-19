package com.itouch8.pump.core.dao.log;

import java.sql.Connection;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class DataSourceSpyInterceptor implements MethodInterceptor {
    private RdbmsSpecifics rdbmsSpecifics = null;

    private RdbmsSpecifics getRdbmsSpecifics(Connection conn) {
        if (this.rdbmsSpecifics == null) {
            this.rdbmsSpecifics = DriverSpy.getRdbmsSpecifics(conn);
        }
        return this.rdbmsSpecifics;
    }

    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object result = invocation.proceed();
        if ((SpyLogFactory.getSpyLogDelegator().isJdbcLoggingEnabled()) && (result instanceof Connection)) {
            Connection conn = (Connection) result;
            return new ConnectionSpy(conn, getRdbmsSpecifics(conn));
        }

        return result;
    }
}
