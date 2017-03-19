package com.itouch8.pump.core.dao.mybatis.mapper;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;

import com.itouch8.pump.core.dao.mybatis.page.PageAdapter;
import com.itouch8.pump.core.util.page.IPage;
import com.itouch8.pump.core.util.page.PageUtils;


public class MapperProxy<T> implements InvocationHandler, Serializable {
    
    private static final long serialVersionUID = 8350388011934632811L;

    private final SqlSession sqlSession;
    private final Class<T> mapperInterface;
    private final Map<Method, MapperMethod> methodCache;
    private Map<Method, Integer> pageParamIndexCache = new HashMap<Method, Integer>();

    public MapperProxy(SqlSession sqlSession, Class<T> mapperInterface, Map<Method, MapperMethod> methodCache) {
        this.sqlSession = sqlSession;
        this.mapperInterface = mapperInterface;
        this.methodCache = methodCache;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (Object.class.equals(method.getDeclaringClass())) {
            Integer index = getPageParamIndex(method);
            if (null != index) {
                Object o = args[index];
                if (null == o) {
                    args[index] = new PageAdapter(PageUtils.createPage());
                } else if (o instanceof IPage) {
                    args[index] = new PageAdapter((IPage) o);
                }
            }
            return method.invoke(this, args);
        }
        final MapperMethod mapperMethod = cachedMapperMethod(method);
        return mapperMethod.execute(sqlSession, args);
    }

    private MapperMethod cachedMapperMethod(Method method) {
        MapperMethod mapperMethod = methodCache.get(method);
        if (mapperMethod == null) {
            mapperMethod = new MapperMethod(mapperInterface, method, sqlSession.getConfiguration());
            methodCache.put(method, mapperMethod);
        }
        return mapperMethod;
    }

    private Integer getPageParamIndex(Method method) {
        if (pageParamIndexCache.containsKey(method)) {
            return pageParamIndexCache.get(method);
        } else {
            final Class<?>[] argTypes = method.getParameterTypes();
            Class<?> rcls = RowBounds.class;
            Class<?> pcls = IPage.class;
            for (int i = 0; i < argTypes.length; i++) {
                if (rcls.isAssignableFrom(argTypes[i]) || pcls.isAssignableFrom(argTypes[i])) {
                    pageParamIndexCache.put(method, i);
                    return i;
                }
            }
            pageParamIndexCache.put(method, null);
            return null;
        }
    }

}
