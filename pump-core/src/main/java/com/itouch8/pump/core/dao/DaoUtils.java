package com.itouch8.pump.core.dao;

import java.util.List;

import com.itouch8.pump.core.PumpConfig;
import com.itouch8.pump.core.dao.mybatis.MybatisUtils;
import com.itouch8.pump.core.util.page.IPage;

public class DaoUtils {

    
    public static String getPumpTableName(String tableName) {
        return PumpConfig.getPumpTablePrefix() + tableName;
    }

    
    public static <T> T selectOne(String sqlId) {
        return getDaoTempate(sqlId).selectOne(sqlId);
    }

    
    public static <T> T selectOne(String sqlId, Object parameter) {
        return getDaoTempate(sqlId).selectOne(sqlId, parameter);
    }

    
    public static <E> List<E> selectList(String sqlId) {
        return getDaoTempate(sqlId).selectList(sqlId);
    }

    
    public static <E> List<E> selectList(String sqlId, Object parameter) {
        return getDaoTempate(sqlId).selectList(sqlId, parameter);
    }

    
    public static <E> List<E> selectList(String sqlId, IPage page) {
        return getDaoTempate(sqlId).selectList(sqlId, page);
    }

    
    public static <E> List<E> selectList(String sqlId, Object parameter, IPage page) {
        return getDaoTempate(sqlId).selectList(sqlId, parameter, page);
    }
    
    public static int insert(String sqlId) {
        return getDaoTempate(sqlId).insert(sqlId);
    }

    
    public static int insert(String sqlId, Object parameter) {
        return getDaoTempate(sqlId).insert(sqlId, parameter);
    }

    
    public static int update(String sqlId) {
        return getDaoTempate(sqlId).update(sqlId);
    }

    
    public static int update(String sqlId, Object parameter) {
        return getDaoTempate(sqlId).update(sqlId, parameter);
    }

    
    public static int delete(String sqlId) {
        return getDaoTempate(sqlId).delete(sqlId);
    }

    
    public static int delete(String sqlId, Object parameter) {
        return getDaoTempate(sqlId).delete(sqlId, parameter);
    }
    
    private static IDaoTemplate getDaoTempate(String sqlId) {
        return MybatisUtils.getDaoTemplate(sqlId);
    }
}
