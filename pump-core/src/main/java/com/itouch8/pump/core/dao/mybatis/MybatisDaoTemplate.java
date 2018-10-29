package com.itouch8.pump.core.dao.mybatis;

import java.io.Reader;
import java.sql.Clob;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.itouch8.pump.core.dao.IDaoTemplate;
import com.itouch8.pump.core.dao.call.ICallResult;
import com.itouch8.pump.core.dao.exception.DaoExceptionCodes;
import com.itouch8.pump.core.dao.jndi.JndiManager;
import com.itouch8.pump.core.dao.mybatis.call.CallAdapter;
import com.itouch8.pump.core.dao.mybatis.page.PageAdapter;
import com.itouch8.pump.core.dao.mybatis.stream.MybatisListStreamReader;
import com.itouch8.pump.core.dao.sql.SqlManager;
import com.itouch8.pump.core.dao.stream.IListStreamReader;
import com.itouch8.pump.core.util.CoreUtils;
import com.itouch8.pump.core.util.exception.Throw;
import com.itouch8.pump.core.util.page.IPage;


public class MybatisDaoTemplate implements IDaoTemplate {

    private static final Logger logger = LoggerFactory.getLogger(MybatisDaoTemplate.class);

    
    private final SqlSessionTemplate sqlSession;

    
    private final SqlSessionTemplate batchSqlSession;

    
    private final ThreadLocal<Boolean> batchType = new ThreadLocal<Boolean>() {
        @Override
        protected Boolean initialValue() {
            return Boolean.FALSE;
        }
    };

    
    private final ThreadLocal<Boolean> prevBatchType = new ThreadLocal<Boolean>();

    private static final ThreadLocal<SqlSession> localBatchSqlSession = new ThreadLocal<SqlSession>();

    
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
    public <E> IListStreamReader<E> selectListStream(String sqlId) {
        return new MybatisListStreamReader<E>(this, sqlId, null);
    }

    
    @Override
    public <E> IListStreamReader<E> selectListStream(String sqlId, Object parameter) {
        return new MybatisListStreamReader<E>(this, sqlId, parameter);
    }

    
    @Override
    public <E> IListStreamReader<E> selectListStream(String sqlId, int fetchSize) {
        return new MybatisListStreamReader<E>(this, sqlId, null, fetchSize);
    }

    
    @Override
    public <E> IListStreamReader<E> selectListStream(String sqlId, Object parameter, int fetchSize) {
        return new MybatisListStreamReader<E>(this, sqlId, parameter, fetchSize);
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

    
    @Override
    public int[] executeBatch(String sqlId, List<?> parameters) {
        List<String> sqlIds = new ArrayList<String>();
        for (int i = 0, s = parameters.size(); i < s; i++) {
            sqlIds.add(sqlId);
        }
        DataSource ds = MybatisUtils.getDataSource(sqlId);
        return this.doExecuteBatch(ds, sqlIds, parameters);
    }

    
    @Override
    public int[] executeBatch(List<String> sqlIds) {
        return executeBatch(sqlIds, null);
    }

    
    @Override
    public int[] executeBatch(List<String> sqlIds, List<?> parameters) {
        if (null == sqlIds) {
            return null;
        } else if (sqlIds.isEmpty()) {
            return ArrayUtils.EMPTY_INT_ARRAY;
        } else if (null != parameters && sqlIds.size() != parameters.size()) {
            throw Throw.createRuntimeException(DaoExceptionCodes.YT020015, sqlIds.size(), null == parameters ? 0 : parameters.size());
        } else {
            String sqlId = sqlIds.get(0);
            DataSource ds = MybatisUtils.getDataSource(sqlId);
            for (int i = 1, s = sqlIds.size(); i < s; i++) {// 确认是同一个数据源
                if (ds != MybatisUtils.getDataSource(sqlIds.get(i))) {// 这里直接比较是否为同一个数据源对象
                    Throw.throwRuntimeException(DaoExceptionCodes.YT020017, SqlManager.getExecuteSqlId(sqlId), SqlManager.getExecuteSqlId(sqlIds.get(i)));
                }
            }
            return this.doExecuteBatch(ds, sqlIds, parameters);
        }
    }

    @Override
    public void openBatchType() {
        this.prevBatchType.set(this.batchType.get());
        this.batchType.set(Boolean.TRUE);
        localBatchSqlSession.set(batchSqlSession);
    }

    @Override
    public void resetExecutorType() {
        Boolean pbt = this.prevBatchType.get();
        if (null == pbt) {
            pbt = Boolean.FALSE;
        }
        this.batchType.set(pbt);
        if (!pbt.booleanValue()) {
            localBatchSqlSession.remove();
        }
        this.prevBatchType.remove();
    }

    public static int[] staticFlushBatch() {
        SqlSession batchSqlSession = localBatchSqlSession.get();
        if (null != batchSqlSession) {
            List<BatchResult> r = batchSqlSession.flushStatements();
            return resolveBatchResult(r);
        } else {
            return null;
        }
    }

    @Override
    public int[] flushBatch() {
        SqlSession batchSqlSession = getExecuteSqlSession();
        List<BatchResult> r = batchSqlSession.flushStatements();
        return resolveBatchResult(r);
    }

    private int[] doExecuteBatch(DataSource dataSource, List<String> sqlIds, List<?> parameters) {
        try {
            if (this.batchType.get()) {
                this.flushBatch();// 为避免影响本次批量执行结果，先执行之前已有的批量（如果有）
            }
            openBatchType();
            SqlSession batchSqlSession = getExecuteSqlSession();
            PlatformTransactionManager txManager = this.getTransactinManager(dataSource);
            if (null == txManager) {
                List<BatchResult> r = doExecuteBatch(sqlIds, parameters, batchSqlSession);
                return resolveBatchResult(r);
            } else {
                DefaultTransactionDefinition def = new DefaultTransactionDefinition();
                def.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
                def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
                TransactionStatus status = txManager.getTransaction(def);
                try {
                    List<BatchResult> r = doExecuteBatch(sqlIds, parameters, batchSqlSession);
                    txManager.commit(status);
                    return resolveBatchResult(r);
                } catch (Throwable e) {
                    txManager.rollback(status);
                    throw Throw.createRuntimeException(e);
                }
            }
        } finally {
            this.resetExecutorType();
        }
    }

    private List<BatchResult> doExecuteBatch(List<String> sqlIds, List<?> parameters, SqlSession batchSqlSession) {
        for (int i = 0, s = sqlIds.size(); i < s; i++) {
            String sqlId = SqlManager.getExecuteSqlId(sqlIds.get(i));
            Object param = parameters == null ? null : parameters.get(i);
            logger.trace("batch " + (i + 1) + " [sqlId : {" + sqlId + "}, param : {" + (param == null ? "" : param.toString()) + "}]");
            batchSqlSession.update(sqlId, param);
        }
        return batchSqlSession.flushStatements();
    }

    
    @Override
    public ICallResult call(String sqlId) {
        return call(sqlId, null);
    }

    
    @Override
    public ICallResult call(String sqlId, Object parameter) {
        CallAdapter adapter = new CallAdapter();
        SqlSession sqlSession = getExecuteSqlSession();
        sqlSession.selectList(SqlManager.getExecuteSqlId(sqlId), parameter, adapter);
        return adapter.getCallResult();
    }

    
    private static int[] resolveBatchResult(List<BatchResult> result) {
        int[] rs = new int[result.size()];
        int i = 0;
        for (BatchResult br : result) {
            int c = 0;
            for (int brr : br.getUpdateCounts()) {
                c += brr;
            }
            rs[i++] = c;
        }
        return rs;
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

    private PlatformTransactionManager getTransactinManager(DataSource dataSource) {
        try {
            return JndiManager.getTransactinManager(dataSource);
        } catch (Exception e) {
            return null;
        }
    }

    private SqlSession getExecuteSqlSession() {
        return this.batchType.get() ? getBatchSqlSession() : getSqlSession();
    }
}
