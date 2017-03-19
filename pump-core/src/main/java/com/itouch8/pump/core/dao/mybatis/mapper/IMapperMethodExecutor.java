package com.itouch8.pump.core.dao.mybatis.mapper;


public interface IMapperMethodExecutor {

    
    public boolean isSupport(MapperMethod mapperMethod);

    
    public Object execute(MapperMethod mapperMethod, Object[] args);
}
