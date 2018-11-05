package com.itouch8.pump.core.dao.mybatis;

import java.io.Reader;
import java.sql.Clob;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;

import com.itouch8.pump.core.dao.IDaoTemplate;
import com.itouch8.pump.core.dao.mybatis.page.PageAdapter;
import com.itouch8.pump.core.dao.sql.SqlManager;
import com.itouch8.pump.core.util.CoreUtils;
import com.itouch8.pump.core.util.page.IPage;


public class MybatisDaoTemplate implements IDaoTemplate {

    private final SqlSessionTemplate sqlSession;

    
    private final SqlSessionTemplate batchSqlSession;

    
    private final ThreadLocal<Boolean> batchType = new ThreadLocal<Boolean>() {
        @Override
        protected Boolean initialValue() {
            return Boolean.FALSE;
        }
    };

    
    public MybatisDaoTemplate(SqlSessionFactory sqlSessionFactory) {
        this.sqlSession = new SqlSessionTemplate(sqlSessionFactory);
        this.batchSqlSession = new SqlSessionTemplate(sqlSessionFactory, ExecutorType.BATCH);
    }

    public SqlSessionTemplate getSqlSession() {
        return sqlSession;
    }

    public SqlSessionTemplate getBatchSqlSession() {
        return batchSqlSession;
    }

    
    @Override
    public <T> T selectOne(String sqlId) {
        return selectOne(sqlId, null);
    }

    
    @Override
    public <T> T selectOne(String sqlId, Object parameter) {
        SqlSession sqlSession = getExecuteSqlSession();
        T rs = sqlSession.<T>selectOne(SqlManager.getExecuteSqlId(sqlId), parameter);
        return convertCamelProperties(rs);
    }

    
    @Override
    public <E> List<E> selectList(String sqlId) {
        return selectList(sqlId, null);
    }

    
    @Override
    public <E> List<E> selectList(String sqlId, Object parameter) {
        final List<E> list = new ArrayList<E>();
        SqlSession sqlSession = getExecuteSqlSession();
        sqlSession.select(SqlManager.getExecuteSqlId(sqlId), parameter, new ResultHandler<E>() {
            public void handleResult(ResultContext<? extends E> context) {
                E result = convertCamelProperties(context.getResultObject());
                list.add(result);
            }
        });
        return list;
    }

    
    @Override
    public <E> List<E> selectList(String sqlId, IPage page) {
        return selectList(sqlId, null, page);
    }

    
    @Override
    public <E> List<E> selectList(String sqlId, Object parameter, IPage page) {
        final RowBounds adapter = new PageAdapter(page);
        final List<E> list = new ArrayList<E>();
        SqlSession sqlSession = getExecuteSqlSession();
        sqlSession.select(SqlManager.getExecuteSqlId(sqlId), parameter, adapter, new ResultHandler<E>() {
            public void handleResult(ResultContext<? extends E> context) {
                E result = convertCamelProperties(context.getResultObject());
                list.add(result);
            }
        });
        return list;
    }

    
    @Override
    public int insert(String sqlId) {
        return update(sqlId);
    }

    
    @Override
    public int insert(String sqlId, Object parameter) {
        return update(sqlId, parameter);
    }

    
    @Override
    public int update(String sqlId) {
        return update(sqlId, null);
    }

    
    @Override
    public int update(String sqlId, Object parameter) {
        SqlSession sqlSession = getExecuteSqlSession();
        return sqlSession.update(SqlManager.getExecuteSqlId(sqlId), parameter);
    }

    
    @Override
    public int delete(String sqlId) {
        return update(sqlId);
    }

    
    @Override
    public int delete(String sqlId, Object parameter) {
        return update(sqlId, parameter);
    }

    
    @SuppressWarnings("unchecked")
    private <E> E convertCamelProperties(E obj) {
        if (obj instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) obj;
            String[] key = new String[map.size()];
            map.keySet().toArray(key);
            for (String k : key) {
                Object value = map.remove(k);
                if (value instanceof Clob) {
                    try {
                        Clob clob = (Clob) value;
                        Reader reader = clob.getCharacterStream(1, clob.length());
                        value = IOUtils.toString(reader);
                    } catch (Exception e) {
                    }
                }
                map.put(CoreUtils.convertToCamel(k), value);
            }
            return CoreUtils.cast(map);
        } else {
            return obj;
        }
    }
    private SqlSession getExecuteSqlSession() {
        return this.batchType.get() ? getBatchSqlSession() : getSqlSession();
    }
}
