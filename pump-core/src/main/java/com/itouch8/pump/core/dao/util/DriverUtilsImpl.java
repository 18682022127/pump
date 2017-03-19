package com.itouch8.pump.core.dao.util;

import java.util.HashMap;
import java.util.Map;

import com.itouch8.pump.core.util.CoreUtils;


public abstract class DriverUtilsImpl {

    private static final DriverUtilsImpl instance = new DriverUtilsImpl() {};

    private DriverUtilsImpl() {}

    static DriverUtilsImpl getInstance() {
        return instance;
    }

    private static final Map<String, Class<?>> driver = new HashMap<String, Class<?>>();

    
    public boolean load(String driverClassName) {
        if (!driver.containsKey(driverClassName)) {
            synchronized (driver) {
                if (!driver.containsKey(driverClassName)) {
                    try {
                        Class<?> cls = CoreUtils.forName(driverClassName);
                        driver.put(driverClassName, cls);
                        return true;
                    } catch (Exception e) {
                        driver.put(driverClassName, null);
                        return false;
                    }
                } else {
                    return null != driver.get(driverClassName);
                }
            }
        } else {
            return null != driver.get(driverClassName);
        }
    }
}
