package com.itouch8.pump.core.util.data.accessor;

import java.util.Map;

import com.itouch8.pump.core.util.config.BaseConfig;


public class DataAccessors {

    public static IDataAccessor newDataAccessor() {
        return BaseConfig.getDataAccessorFactory().newDataAccessor();
    }

    public static IDataAccessor newDataAccessor(Object root) {
        return BaseConfig.getDataAccessorFactory().newDataAccessor(root);
    }

    public static IDataAccessor newDataAccessor(Object root, Map<String, Object> vars) {
        return BaseConfig.getDataAccessorFactory().newDataAccessor(root, vars);
    }
}
