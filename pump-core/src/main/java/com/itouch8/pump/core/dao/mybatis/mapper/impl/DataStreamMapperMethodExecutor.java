package com.itouch8.pump.core.dao.mybatis.mapper.impl;

import com.itouch8.pump.core.dao.mybatis.mapper.IMapperMethodExecutor;
import com.itouch8.pump.core.dao.mybatis.mapper.MapperMethod;
import com.itouch8.pump.core.dao.mybatis.mapper.MapperMethod.SqlCommand;
import com.itouch8.pump.core.dao.stream.IListStreamReader;


public abstract class DataStreamMapperMethodExecutor implements IMapperMethodExecutor {

    private static final ThreadLocal<IListStreamReader<?>> streams = new ThreadLocal<IListStreamReader<?>>();

    
    @Override
    public boolean isSupport(MapperMethod mapperMethod) {
        return mapperMethod.getMethodSignature().returnsMany() && isSupport();
    }

    abstract protected boolean isSupport();

    protected int getFetchSize() {
        return 1000;
    }

    @Override
    public Object execute(MapperMethod mapperMethod, Object[] args) {
        SqlCommand command = mapperMethod.getCommand();
        String sqlId = command.getName();
        Object parameter = mapperMethod.getMethodSignature().convertArgsToSqlCommandParam(args);
        int fetchSize = getFetchSize();
        IListStreamReader<?> reader = command.getDao().selectListStream(sqlId, parameter, fetchSize);
        streams.set(reader);
        return null;
    }

    public static IListStreamReader<?> getDataStreamReader() {
        return streams.get();
    }

    public static void clearDataStreamReader() {
        streams.remove();
    }
}
