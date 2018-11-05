package com.itouch8.pump.core.dao.mybatis.interceptor.statement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.RowBounds;
import org.springframework.data.domain.Page;

import com.itouch8.pump.core.dao.dialect.IDialect;
import com.itouch8.pump.core.dao.jndi.JndiManager;
import com.itouch8.pump.core.dao.mybatis.interceptor.AbstractInterceptor;
import com.itouch8.pump.core.dao.mybatis.page.PageAdapter;
import com.itouch8.pump.core.dao.mybatis.parameter.ExpressionParameterHandler;
import com.itouch8.pump.core.util.CoreUtils;
import com.itouch8.pump.core.util.exception.Throw;
import com.itouch8.pump.core.util.page.IPage;


@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class,Integer.class})
        // @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class,
        // Integer.class })
})
public class StatementHandlerInterceptor extends AbstractInterceptor {

    
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler handler = super.getTarget(invocation, StatementHandler.class);
        if (handler instanceof RoutingStatementHandler) {
            handler = (StatementHandler) CoreUtils.getProperty(handler, "delegate");
        }
        MetaObject meta = SystemMetaObject.forObject(handler);

        // 拦截SQL：SQL执行参数替换,SQL语句静态替换
        BoundSql boundSql = handler.getBoundSql();
        Object parameterObject = boundSql.getParameterObject();
        MappedStatement mappedStatement = null;
        ParameterHandler ph = handler.getParameterHandler();
        if (!(ph instanceof ExpressionParameterHandler)) {
            mappedStatement = (MappedStatement) meta.getValue("mappedStatement");
            ph = new ExpressionParameterHandler(mappedStatement, parameterObject, boundSql);
            meta.setValue("parameterHandler", ph);
        } else {
            mappedStatement = ((ExpressionParameterHandler) ph).getMappedStatement();
        }
        
        String sql = boundSql.getSql();
        sql = CoreUtils.formatWhitespace(sql);// 格式化SQL字符串
        meta.setValue("boundSql.sql", sql);
        
        IPage page = findPageObject(parameterObject);

        if (null != page) {
            Connection conn = (Connection) invocation.getArgs()[0];
            IDialect dialect = JndiManager.getDialect(conn);// 数据库方言
            setPageProperties(page, mappedStatement.getStatementLog(), conn, ph, dialect, sql);
            // 分页SQL
            String pageSql = dialect.getScopeSql(sql, page.getStart(), page.getPageSize());
            meta.setValue("boundSql.sql", pageSql);
        }
        return invocation.proceed();
    }
    
    
    public IPage findPageObject(Object parameterObj) {
        if (parameterObj instanceof Page<?>) {
            return (IPage) parameterObj;
        } else if (parameterObj instanceof Map) {
            for (Object val : ((Map<?, ?>) parameterObj).values()) {
                if (val instanceof IPage) {
                    return (IPage) val;
                }
            }
        }
        return null;
    }

    
    private void setPageProperties(IPage page, Log log, Connection conn, ParameterHandler handler, IDialect dialect, String sql) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            long total = page.getTotalRecords();
            if (page.isNeedCalTotal()) {
                pstmt = conn.prepareStatement(dialect.getTotalSql(sql));
                handler.setParameters(pstmt);
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    total = rs.getInt(1);
                }
                if (null != log) {
                    log.debug("<==      Total:(Page) calculate page totals [" + total + "]");
                }
            } else if (null != log) {
                log.debug("<==      Total:(Page) use old page totals [" + total + "]");
            }
            page.setPageProperty(total);
        } catch (SQLException e) {
            Throw.throwRuntimeException(e);
        } finally {
            try {
                if (null != rs) {
                    rs.close();
                }
            } catch (Exception e) {
            }
            try {
                if (null != pstmt) {
                    pstmt.close();
                }
            } catch (Exception e) {
            }
        }
    }
}
