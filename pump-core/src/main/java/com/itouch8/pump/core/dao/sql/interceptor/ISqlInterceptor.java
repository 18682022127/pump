package com.itouch8.pump.core.dao.sql.interceptor;

import com.itouch8.pump.core.dao.jndi.IJndi;


public interface ISqlInterceptor {

    
    public String intercept(IJndi jndi, String src, Object root);
}
