package com.itouch8.pump.core.dao;

import java.util.List;

import com.itouch8.pump.core.dao.call.ICallResult;
import com.itouch8.pump.core.dao.stream.IListStreamReader;
import com.itouch8.pump.core.util.page.IPage;


public interface IDaoTemplate {

    
    public <T> T selectOne(String sqlId);

    
    public <T> T selectOne(String sqlId, Object parameter);

    
    public <E> List<E> selectList(String sqlId);

    
    public <E> List<E> selectList(String sqlId, Object parameter);

    
    public <E> List<E> selectList(String sqlId, IPage page);

    
    public <E> List<E> selectList(String sqlId, Object parameter, IPage page);

    
    public <E> IListStreamReader<E> selectListStream(String sqlId);

    
    public <E> IListStreamReader<E> selectListStream(String sqlId, Object parameter);

    
    public <E> IListStreamReader<E> selectListStream(String sqlId, int fetchSize);

    
    public <E> IListStreamReader<E> selectListStream(String sqlId, Object parameter, int fetchSize);

    
    public int insert(String sqlId);

    
    public int insert(String sqlId, Object parameter);

    
    public int update(String sqlId);

    
    public int update(String sqlId, Object parameter);

    
    public int delete(String sqlId);

    
    public int delete(String sqlId, Object parameter);

    
    public int[] executeBatch(String sqlId, List<?> parameters);

    
    public int[] executeBatch(List<String> sqlIds);

    
    public int[] executeBatch(List<String> sqlIds, List<?> parameters);

    
    public void openBatchType();

    
    public void resetExecutorType();

    
    public int[] flushBatch();

    
    public ICallResult call(String sqlId);

    
    public ICallResult call(String sqlId, Object parameter);
}
