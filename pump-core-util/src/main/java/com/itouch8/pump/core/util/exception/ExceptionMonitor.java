package com.itouch8.pump.core.util.exception;

import com.itouch8.pump.core.util.config.BaseConfig;
import com.itouch8.pump.core.util.monitor.IMonitor;


public class ExceptionMonitor {

    
    public static boolean isMonitoring() {
        IMonitor monitor = getMonitor();
        return null == monitor ? false : monitor.isMonitoring();
    }

    
    public static void start() {
        IMonitor monitor = getMonitor();
        if (null != monitor) {
            monitor.start();
        }
    }

    
    public static void stop() {
        IMonitor monitor = getMonitor();
        if (null != monitor) {
            monitor.stop();
        }
    }

    
    private static IMonitor getMonitor() {
        return BaseConfig.getExceptionMonitor();
    }
}
