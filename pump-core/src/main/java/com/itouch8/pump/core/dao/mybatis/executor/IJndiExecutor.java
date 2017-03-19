package com.itouch8.pump.core.dao.mybatis.executor;

import org.apache.ibatis.executor.Executor;

import com.itouch8.pump.core.dao.jndi.IJndi;

public interface IJndiExecutor extends Executor {

    public IJndi getJndi();
}
