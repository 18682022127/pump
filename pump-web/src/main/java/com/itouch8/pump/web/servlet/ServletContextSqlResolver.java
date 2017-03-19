package com.itouch8.pump.web.servlet;

import com.itouch8.pump.core.dao.jndi.IJndi;
import com.itouch8.pump.core.dao.sql.resolver.impl.AbstractSqlResolver;
import com.itouch8.pump.core.util.CoreUtils;


public class ServletContextSqlResolver extends AbstractSqlResolver {

    @Override
    public boolean isSupport(IJndi jndi, String expression) {
        return null != expression && (expression.startsWith("request.") || expression.startsWith("session."));
    }

    @Override
    public Object resolver(IJndi jndi, Object parameterObject, String expression) {
        String exp = "";
        Object root = null;
        if (expression.startsWith("request.params.")) {// 请求参数直接返回
            exp = expression.substring(15);
            return ServletHelp.getRequest().getParameter(exp);
        } else if (expression.startsWith("request.")) {// 请求属性
            exp = expression.substring(8);
            root = ServletHelp.getRequest().getAttribute(exp);
        } else if (expression.startsWith("session.")) {// 会话属性
            exp = expression.substring(8);
            root = ServletHelp.getSession().getAttribute(exp);
        }

        if (null == root) {
            return null;
        } else {
            int index = exp.indexOf('.');
            if (-1 == index) {
                return root;
            } else {
                return CoreUtils.getProperty(root, exp.substring(index + 1));
            }
        }
    }

}
