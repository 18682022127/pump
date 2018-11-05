package com.itouch8.pump.core.dao;

import java.util.List;

import com.itouch8.pump.core.util.page.IPage;


public interface IDaoTemplate {

    
    public <T> T selectOne(String sqlId);

    
    public <T> T selectOne(String sqlId, Object parameter);

    
    public <E> List<E> selectList(String sqlId);

    
    public <E> List<E> selectList(String sqlId, Object parameter);

    
    public <E> List<E> selectList(String sqlId, IPage page);

    
    public <E> List<E> selectList(String sqlId, Object parameter, IPage page);

    public int insert(String sqlId);

    
    public int insert(String sqlId, Object parameter);

    
    public int update(String sqlId);

    
    public int update(String sqlId, Object parameter);

    
    public int delete(String sqlId);

    
    public int delete(String sqlId, Object parameter);

}
