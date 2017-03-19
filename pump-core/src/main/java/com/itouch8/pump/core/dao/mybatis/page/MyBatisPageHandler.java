package com.itouch8.pump.core.dao.mybatis.page;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.apache.ibatis.session.SqlSession;

import com.itouch8.pump.core.util.page.PageUtils;

public class MyBatisPageHandler implements InvocationHandler {

    private SqlSession sqlSession = null;

    private String pageKey = null;

    public MyBatisPageHandler(SqlSession sqlSession, String pageKey) {
        this.sqlSession = sqlSession;
        this.pageKey = pageKey;
    }

    // public MyBatisPageHandler(SqlSession sqlSession) {
    // this(sqlSession, null);
    // }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = null;
        String sqlId = method.getDeclaringClass().getCanonicalName() + "." + method.getName();
        // RowBounds rowBounds = PageRowBound.getPageRowBound();
        PageAdapter pa = new PageAdapter(PageUtils.createPage(pageKey));

        if (args == null || args.length == 0) {
            result = sqlSession.selectList(sqlId, null, pa);
        } else if (args.length == 1) {
            result = sqlSession.selectList(sqlId, args[0], pa);
        } else {
            result = sqlSession.selectList(sqlId, args, pa);
        }
        return result;
    }
}
